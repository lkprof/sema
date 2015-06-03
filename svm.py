from matplotlib import pyplot
import scipy as sp
import numpy as np
from matplotlib import pylab
from sklearn.datasets import load_files
from sklearn.datasets import load_svmlight_file
from sklearn.cross_validation import train_test_split
from sklearn.feature_extraction.text import  CountVectorizer
from sklearn.feature_extraction.text import  TfidfVectorizer
from sklearn.naive_bayes import MultinomialNB
from sklearn.metrics import precision_recall_curve, roc_curve, auc
from sklearn.metrics import classification_report
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import make_scorer
from sklearn import cross_validation as c_v
from sklearn import grid_search as g_s
from sklearn.multiclass import OneVsRestClassifier
from sklearn.feature_selection import chi2,f_classif,SelectKBest,SelectPercentile
import time
import __future__
from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import SVC

start_time = time.time()


# generate result
def svm():
    #load data
    x_train,y_train=load_svmlight_file("12trainset")
    x_train.todense()
    x_test,y_test=load_svmlight_file("12testdata")
    x_test.todense()
    sk=SelectKBest(f_classif,9).fit(x_train,y_train)
    x_new=sk.transform(x_train)
    x_newtest=sk.transform(x_test)
    print(sk.scores_)
    print(x_new.shape)
    print(sk.get_support())
    #classfier
    clf=SVC(C=2,gamma=2)
    ovrclf=OneVsRestClassifier(clf,-1)
    ovrclf.fit(x_train,y_train)
    y_pred=ovrclf.predict(x_test)
    # write result
    with open("result.txt","w") as fw:
        for st in y_pred.tolist():
            fw.write(str(st)+'\n')
    print(np.array(y_pred).shape)

    target_names=['0','1','2','3']
    #result
    #sum_y = np.sum((np.array(y_pred)-np.array(y_test))**2)
    #print(classification_report(y_test,y_pred,target_names=target_names))
    #print("sougouVal: ",float(sum_y)/y_pred.shape[0])
    print(time.time()-start_time)

# grid search
def test():
    x_train,y_train=load_svmlight_file("D:/traindata/12trainset")
    x_train.todense()
    x_test,y_test=load_svmlight_file("D:/traindata/12testset")
    x_test.todense()
    print(x_train.shape)
    #classifier
    clf=SVC(kernel='rbf')
    ovrclf=OneVsRestClassifier(clf,-1)
    #parameter
    parameters=[{'estimator__C':[2**-5,2**-4,2**-3,2**-2,2**-1,1,2**1,2**2,2**3,2**4,2**5],
                 'estimator__kernel':['rbf'],
                 'estimator__gamma':[2**-5,2**-4,2**-3,2**-2,2**-1,1,2**1,2**2,2**3,2**4,2**5]},
                {'estimator__C':[2**-5,2**-4,2**-3,2**-2,2**-1,1,2**1,2**2,2**3,2**4,2**5],
                 'estimator__kernel':['linear']}]
    para={'estimator__C':[2**-5,2**-4],
                 'estimator__kernel':['rbf'],
                 'estimator__gamma':[2**-1,1]}
    #scoring
    sougou_score=make_scorer(score_func,greater_is_better=False)
    #cross_validation iterator
    sfk=c_v.StratifiedKFold(y_train,shuffle=True,n_folds=5,random_state=0)
    #grid search
    gsclf=g_s.GridSearchCV(ovrclf,param_grid=para,cv=sfk,scoring=sougou_score)
    gsclf.fit(x_train,y_train)
    print("best score: ",gsclf.best_score_)
    print("best parameters: ",gsclf.best_params_)
    y_pred=gsclf.predict(x_test)

    #result
    target_names=['0','1','2','3']
    sum_y = np.sum((np.array(y_pred)-np.array(y_test))**2)
    print(classification_report(y_test,y_pred,target_names=target_names))
    print("sougouVal: ",float(sum_y)/y_pred.shape[0])
    print(time.time()-start_time)

    #scores=c_v.cross_val_score(ovrclf,x_train,y_train,cv=sfk,scoring='accuracy')
    #print(scores)

def score_func(y_pred,y_test):
    sum_y = np.sum((np.array(y_pred)-np.array(y_test))**2)
    sougouVal=float(sum_y)/y_pred.shape[0]
    return sougouVal


if __name__ == '__main__':
    svm()
    #test()
