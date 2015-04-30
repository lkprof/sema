#coding=utf-8

import sys
reload(sys)
sys.setdefaultencoding('utf-8')

import jieba
import jieba.posseg as pseg
import time
import sys

def strQ2B(ustring):
    """全角转半角"""
    rstring = ""
    for uchar in ustring:
        inside_code=ord(uchar)
        if inside_code == 12288:                              #全角空格直接转换
            inside_code = 32
        elif (inside_code >= 65281 and inside_code <= 65374): #全角字符（除空格）根据关系转化
            inside_code -= 65248
        rstring += unichr(inside_code)
    return rstring

def format_text():
    row = ''
    with open('sougou_2015.txt','r') as f,open('sougou_format.txt','w') as fw:
        for line in f:
            linelist = line.split('\t')
            linelist[1] = linelist[1].decode('utf8')
            format_str = strQ2B(linelist[1])
            format_str = format_str.encode('utf8')
            format_str = str(format_str)
            #format_str = format_str.replace(' ','')
            print format_str
            row = str(linelist[0] +'\t'+ format_str +'\t'+ linelist[2])
            fw.write(row)

def match_score(query, title):
    #print query,title
    query = query.split(' ')
    title = title.split(' ')
    word_num = 0
    for word_in_query in query:
        if word_in_query in title:
            word_num += 1

    return (word_num**2.0)/(len(query)*len(title))

def word_distance_score(query, title):
    #print query,title
    query = query.strip()
    title = title.strip()
    word_distance = 1
    distance_sum = 0
    word_in_query_dis_sum = 0
    order_distance_sum = 0
    query_in_title = []
    word_in_query_dis = 0

    query = query.split(' ')
    title = title.split(' ')

    for word_in_query in query:
        if word_in_query in title:
            query_in_title.append(word_in_query)
    if len(query_in_title)==0:
        return (0, 0)
    elif len(query_in_title)==1:
        k = float(len(query_in_title))/len(query)
        return (1*k, 1*k)
    else:
        k = float(len(query_in_title))/len(query)
        for word_pos in range(len(query_in_title)-1):
            #print query_in_title[word_pos+1],query_in_title[word_pos]
            word_in_query_dis = query.index(query_in_title[word_pos+1]) - query.index(query_in_title[word_pos])
            word_in_title_dis = title.index(query_in_title[word_pos+1]) - title.index(query_in_title[word_pos])
            #print word_in_title_dis
            if word_in_title_dis>0:
                distance_sum += word_in_title_dis
                word_in_query_dis_sum += word_in_query_dis
            if word_in_title_dis<0:
                order_distance_sum += word_in_title_dis
        title_distance = len(query_in_title) - 1
        if distance_sum==0:
            distance = 1
        else:
            if float(word_in_query_dis_sum)<float(distance_sum):
                distance = float(word_in_query_dis_sum)/distance_sum
            else:
                distance = float(distance_sum)/word_in_query_dis_sum
        if order_distance_sum==0:
            order = 1
        else:
            order = float(title_distance)/(title_distance+abs(order_distance_sum))
        return (distance*k, order*k)

def grade_zero_three(query,title):
    query = query.split(' ')
    title = title.split(' ')
    query_in_title = []

    for word_in_query in query:
        if word_in_query in title:
            query_in_title.append(word_in_query)
    if len(set(query).intersection(set(title)))==0:
        return 0
    if float(len(query_in_title))/len(query) < 0.65:
        return 1
    if float(len(query_in_title))/len(query) >= 0.65:
        if set(query).issubset(set(title)):
            if float(len(query))/len(title)>0.9:
                return 3
            else:
                return 2
        else:
            return 2

def position_weight():
    position = 0
    noun_verb_weight = 3.0
    adv_adj_weight = 1.5
    others_weight = 0.5

    p_weight = position*position - position + 2
    weight = (who_weight + p_weight)*TFIDF

def len_weight(query,title):
    query = query.split(' ')
    title = title.split(' ')

    query_len = len(query)
    title_len = len(title)
    len_sim = 1 - abs(query_len - title_len)/(query_len+title_len)
    with open('weight_len.txt', 'w') as f:
        f.write(len_sim + '\n')

    return len_sim

def noun_verb_weight(query,title):
    noun_num1 = query.count('/n')
    noun_num2 = title.count('/n')
    verb_num1 = query.count('/v')
    verb_num2 = title.count('/v')
    noun_sim = 1 - abs(noun_num1 - noun_num2)/(noun_num1 + noun_num2)
    verb_sim = 1 - abs(verb_num1 - verb_num2)/(verb_num1 + verb_num2)
    with open('weight_noun_verb.txt', 'w') as f:
        f.write(noun_sim + '\t' + verb_sim + '\n')
    return (noun_sim, verb_sim)

def jieba_cut():
    sys.path.append("../")
    jieba.load_userdict("train_nz.dict")
    start_time = time.time()

    with open('has_point.txt','r') as f:
        string=f.read()
        words = pseg.cut(string)                            #进行分词
        result = ''                                         #记录最终结果的变量
        result_flag = ''
        for w in words:
            result += str(w.word) + ' '
            # result_flag += str(w.word) + ' ' + '/' + str(w.flag) + ' '    #加词性标注
        #print result
    with open('has_point_nz_cut.txt','w') as fw:                 #将结果保存到另一个文档中
        fw.write(result)
    # with open('train_sample_no_point_no_tail_flag.txt','w') as fw:                 #将结果保存到另一个文档中
    #     fw.write(result_flag)

    end_time = time.time()
    print '分词及打分标注完成，耗时：'+str(end_time - start_time)+'秒。'   #统计运行时间

def add_dict_process():
    with open('4000s.dict', 'r') as f:
        front_str = ''
        for line in f:
            strlist = line.split(' ')
            strlinefirst = strlist[0]
            front_str += strlinefirst + ' 3 nz' + '\n'
    with open('train.dict', 'r') as fr:
        write_str = ''
        for line in fr:
            # strline = str(line).strip()
            write_str += line[:-1] + ' 3 nz' + '\n'
    write_str = front_str + write_str
    with open('train_nz.dict', 'w') as fw:
        fw.write(write_str)

def jieba_sougou_main():
    two_grade = ''
    with open('has_point_nz_cut.txt','r') as fr:
        for line in fr:
            query_title = line.split('\t')
            # print query_title
            query = query_title[0]
            title = query_title[1]
            # train_grade = query_title[2].strip()
            (distance, order) = word_distance_score(query, title)
            with open('test_distance_score_new.txt','a') as fa:
                fa.write(str(distance) + '\t' + str(order) + '\n')
    #         grade = grade_zero_three(query, title)
    #         two_grade +=  str(train_grade) + '\t' +str(grade) + '\n'
    # with open('two_grade.txt','w') as fw:
    #     fw.write(two_grade)
    # with open('two_grade.txt','r') as fr:
    #     grade_count = 0
    #     zero_count = 0
    #     one_count = 0
    #     two_count = 0
    #     three_count = 0
    #     for line in fr:
    #         fir_sec_grade = line.split('\t')
    #         fir_grade = fir_sec_grade[0]
    #         sec_grade = fir_sec_grade[1]
    #         if int(fir_grade)==int(sec_grade):
    #             grade_count += 1
    #         if int(fir_grade)==int(0):
    #             if int(fir_grade)==int(sec_grade):
    #                 zero_count += 1
    #         if int(fir_grade)==int(1):
    #             if int(fir_grade)==int(sec_grade):
    #                 one_count += 1
    #         if int(fir_grade)==int(2):
    #             if int(fir_grade)==int(sec_grade):
    #                 two_count += 1
    #         if int(fir_grade)==int(3):
    #             if int(fir_grade)==int(sec_grade):
    #                 three_count += 1
    #     print float(grade_count)/22591
    #     print float(zero_count)/3729
    #     print float(one_count)/8284
    #     print float(two_count)/8288
    #     print float(three_count)/2290
if __name__ == '__main__':
    # jieba_cut()
    # add_dict_process()
    jieba_sougou_main()
