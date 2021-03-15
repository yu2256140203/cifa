package cn.team.nfaqd.test;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.HTMLDocument;
import java.util.*;
import java.util.Scanner;
@SpringBootApplication
@RestController
public class NFA {
    public static   int Num_char=0;
    public static   int change_char_num=1;
    public static   char [] change_char;
    public static   int Num_node=0;
    public static   int [] node;
    public static   char change[][];
    public static    int s, e;
    public  static   char c;
    public  static   Boolean [][][]a;
    public  static   Boolean ArrowFlag=false,ArrowCircleFlag=false;
    public static    Set <Integer>node_epsilon[];
    public static  Matrix []matrix;
    public static  Queue<Integer>que;
    @PostMapping("/NFA_def")
    @ResponseBody
    public String NFA_TransferOfNum(int TransferOfNum,char[] TransferOneByOne,int NodeOfNum,@RequestBody TransferArrow transferArrows[]) throws JsonProcessingException {
        Num_char = TransferOfNum+1;//除去e的字母个数
        Creat(Num_char);//创建Num_char大小的change_char字母数组
//        return Num_char;

        for(int i =1;i<Num_char;i++)
        {
            // System.out.println(Num_char);
            change_char[i] = TransferOneByOne[i-1];
        }
        Num_node = NodeOfNum;//节点的个数
        node = new int[Num_node];
        for (int i = 0; i < Num_node; ++i) node[i] = i;
        change = new char[Num_node][Num_node];
        for(int i=0;i<Num_node;i++)
        {
            for(int j=0;j<Num_node;j++)
            {
                change[i][j]='#';
            }
        }
//        return Num_node;
            a = new Boolean[Num_node ][Num_node][Num_char];
            for (int i = 0; i < Num_node; i++) {
                for (int j = 0; j < Num_node; j++) {
                    for (int k = 0; k < Num_char; k++) {
                        a[i][j][k] = false;
                    }
                }
            }
            int flag=0;
            for(int i =0;flag!=1;i++) {
                s = transferArrows[i].start;
                e = transferArrows[i].end;
                c = transferArrows[i].char_ls.charAt(0);
                int num_node_ls = 0;

                if ( c == '#') {
                    flag = 1;
                    break;
                } else {
                    for (int j = 0; j < Num_char; j++) {
                        if (change_char[j] == c) {
                            num_node_ls = j;
//                        System.out.println(num_node_ls);
                            break;
                        }
                    }
                    a[s][e][num_node_ls] = true;


                    change[s][e] = c;
                }

//                return s+" "+e+" "+c;}
            }
           Result result = new Result();
//            result.PrintMatrix = PrintMatrix();
            result.Calculate = calculate();
            result.PrintResultJSon = PrintResultJson();
            ObjectMapper mapper = new ObjectMapper();
            String result_str = mapper.writeValueAsString(result);
            return result_str;

    }
    public void Creat(int Num_char)
    {
        change_char = new char[Num_char];
        change_char[0]='e';//直接将e-closure插入，避免二次输入
        change_char_num=0;
    }
//    @PostMapping("/NFA_TransferOneByOne")
//    @ResponseBody
//    public char NFA_TransferOneByOne(char TransferOneByOne)
//    {
//        if(change_char_num<Num_char-1)
//        {
//            change_char_num++;
//        // System.out.println(Num_char);
//        change_char[change_char_num] = TransferOneByOne;
//
//        return change_char[change_char_num];
//
//        }
//        else
//            return '#';
//
//
//    }
//    @PostMapping("/NFA_NodeOfNum")
//    @ResponseBody
//    public int NFA_NodeOfNum(int NodeOfNum)
//    {   ArrowFlag=false;
//        Num_node = NodeOfNum;//节点的个数
//        node = new int[Num_node];
//        for (int i = 0; i < Num_node; ++i) node[i] = i;
//        change = new char[Num_node][Num_node];
//        for(int i=0;i<Num_node;i++)
//        {
//            for(int j=0;j<Num_node;j++)
//            {
//                change[i][j]='#';
//            }
//        }
//        return Num_node;
//    }
//    @PostMapping("/NFA_TransferArrow")
//    @ResponseBody
//    public  String NFA_TransferArrow(int start,int end,char char_ls)
//    {   if(ArrowFlag==false) {
//        System.out.println(ArrowFlag);
//        ArrowCircleFlag=false;
//        a = new Boolean[Num_node + 1][Num_node + 1][Num_char];
//        for (int i = 0; i <= Num_node; i++) {
//            for (int j = 0; j <= Num_node; j++) {
//                for (int k = 0; k < Num_char; k++) {
//                    a[i][j][k] = false;
//                }
//            }
//        }
//        ArrowFlag=true;
//    }
//    if(ArrowCircleFlag==false){
//        s=start;
//        e=end;
//        c=char_ls;
//        int num_node_ls =0;
//
//        if (s == 0 && e == 0 && c == '#') {ArrowCircleFlag=true;
//            return "End";}
//        else{
//        for(int i=0;i<Num_char;i++)
//            {
//                if(change_char[i]==c)
//                {
//                    num_node_ls=i;
//                    System.out.println(num_node_ls);
//                }
//            }
//            a[s][e][num_node_ls]=true;
//
//
//            change[s][e] = c;
//
//        return s+" "+e+" "+c;}
//    }
//    else
//        return "End";
//    }
//    @GetMapping("/PrintMatrix")
//    @ResponseBody
    public  String PrintMatrix() throws JsonProcessingException {
//        int Matrix_Num=0;
//        System.out.println(Num_node);
//        matrix=new Matrix[Num_node*Num_node];
//        for(int i=0;i<Num_node*Num_node;i++)
//        {
//            matrix[i] = new Matrix();
//        }
//
//        for (int i = 0; i < Num_node; ++i) {
//            for (int j = 0; j < Num_node; ++j) {
//                matrix[Matrix_Num].xAxis=i;
//                matrix[Matrix_Num].yAxis=j;
//               matrix[Matrix_Num].OwnedLetters=change[i][j];
//               Matrix_Num++;
//            }
//        }
//        System.out.println(1);
        ObjectMapper mapper = new ObjectMapper();
        String str1 = mapper.writeValueAsString(a);
        System.out.println(a);
        return  str1;
    }
//    @GetMapping("/Calculate")
//    @ResponseBody
    public List calculate() throws JsonProcessingException {
        List<Set> list=new ArrayList<>();
//        Set <Integer>node_epsilon[];//建立每个节点的e闭包set，便于之后的扩充
        node_epsilon=new HashSet[Num_node];
        for(int i=0;i<Num_node;i++)
        {
            node_epsilon[i] = new HashSet<Integer>();
        }
        for (int i = 0; i < Num_node; i++) {
            node_epsilon[i].add(i);
            que=new LinkedList<Integer>();
            que.offer(i);//使用一个队列维护来进行宽度优先搜索（先找一步可达的所有位置）
            while (!que.isEmpty()) {
                int ii = que.element();
                int flag=0;
                for (int j = 0; j < Num_node; j++) {
                    if (a[ii][j][0] == true/*&&node_epsilon[i].find(j)==node_epsilon[i].end()*/) {
                        node_epsilon[i].add(j);
//                        if(ii!=j)
//                        if(flag==0)
                            que.offer(j);
//                         if(ii==j) flag=1;

                    }
                }
                que.remove();
            }
            list.add(node_epsilon[i]);


        }
        ObjectMapper mapper = new ObjectMapper();
        String str1 = mapper.writeValueAsString(list);
//        System.out.println(str1);
        return  list;
    }


    public static  String StrResult = "[";
//    @GetMapping("/PrintResultJson")
//    @ResponseBody
    public List PrintResultJson() throws JsonProcessingException {
        List <Vector> list=new ArrayList<>();
        Vector<Set<Integer>> v;
        v= new Vector<>();
        v.add(node_epsilon[0]);
        int it=0;

        while(it<v.size())
        {     Vector<Set<Integer>> VecResult = new Vector<>();
//            System.out.print("s");System.out.print(it);System.out.print(":");
              VecResult.add(v.get(it));



            for(int i=1;i<Num_char;i++)//对字母
            {

                Set <Integer>temp[];//临时集合，得到一个元素的运算
                temp=new HashSet[Num_node];
                Set <Integer>temp2[];
                temp2=new HashSet[Num_node];
                for(int j=0;j<Num_node;j++)
                {
                    temp[j]=new HashSet<>();
                    temp2[j]=new HashSet<>();
                }

                    int j=0;
                Iterator<Integer> bl=v.get(it).iterator();
                int pos=0;
                    while(bl.hasNext()) {

                        int ii=bl.next();//起点结点
                        //v.get(cx).remove(ii);
                        for (int k = 0; k < Num_node; k++)//对集合中每一个元素进行运算,终点
                        {
                            if (a[ii][k][i] == true) {
                                temp[j].add(k);//待定储存动作
                               // System.out.println(k);
                                temp2[pos].add(k);
                            }
                        }
                        if(temp2[pos].size()==0)
                        {
//                            System.out.print("{null}");
                            temp2[pos].add(-1);

                        }
                        //print(temp2[pos]);                  //加
                        VecResult.add(temp2[pos]);
                        pos ++;                             //加
                    }

                    VecResult.add(temp[j]);
                    //求新集合的闭包运算
                    j++;
                    Iterator<Integer> bl2=temp[j-1].iterator();
//                int pos2 =0;                                   //加
//                Set <Integer>temp3[];                                           //加
//                temp3=new HashSet[Num_node];                                    //加
//                for(int t=0;t<Num_node;t++)                          //jia
//                {
//                    temp3[t]=new HashSet<>();
//
//                }
                    while(bl2.hasNext())
                    {
                        int number=bl2.next();
                        temp[j].add((number));//加入原有的元素
//                        temp3[pos2].add(number);
                        Queue<Integer>que=new LinkedList<>();
                        que.offer(number);
                        int flag=0;
                        while (!que.isEmpty()) {
                            int ii = que.element();

                            for (int k = 0; k < Num_node; ++k) {
                                if (a[ii][k][0] == true) {
                                    temp[j].add(k);
                                    if(ii==k&&flag==0)
                                    {que.offer(k);flag=1;}
                                }
                            }
                            que.remove();
                        }
//                        VecResult.add(temp3[pos2]);          //jia
//                        pos2++;                         //jia


                    }
                    VecResult.add(temp[j]);
                //判断该状态集合以前是否存在
                boolean exests =false;
                for(int q=0;q<v.size();q++)
                {
                    if(equal(v.get(q),temp[j]))
                    {
                        exests=true;
                        break;
                    }
                }
                if(!exests)
                {
                    v.add(temp[j]);
                }
            }
            ++it;
            list.add(VecResult);
        }
        ObjectMapper mapper = new ObjectMapper();
        String str1 = mapper.writeValueAsString(list);
        System.out.println(str1);
        System.out.println(1);
        return list;
    }


//    static  void print( Set a)
//    {
//        if (a.isEmpty() == true)return;
//        //cout<<a.size();
//        System.out.print('{');
//        Iterator<Integer> it = a.iterator();
//        while(it.hasNext())
//        {
//            int num=it.next();
//            System.out.print(num);
//            System.out.print(',');
//        }
//        System.out.print("\b}");
//
//    }

    public static boolean  equal(Set<Integer>a,Set<Integer>b)
    {
        if(a==null||b==null) return false;
        if(a.size()!=b.size())
            return false;
        return a.containsAll(b);
    }
}


