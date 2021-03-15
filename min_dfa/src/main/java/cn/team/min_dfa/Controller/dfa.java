package cn.team.min_dfa.Controller;


import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;

public class dfa
{
    private ArrayList<String> SignList; //符号列表

    private ArrayList<String> DFA_StateList; //DFA状态列表
    private int DFA_StartState; //DFA开始状态
    private ArrayList<Integer> DFA_EndList; //DFA结束状态列表
    private ArrayList<ArrayList<Integer>> DFA_AdjacencyTable; //DFA邻接表

    private LinkedList<ArrayList<Integer>> MDFA_Group; //最小化DFA的分组
    private int MDFA_StartState; //最小化DFA的开始状态
    private ArrayList<Integer> MDFA_EndList; //最小化DFA的结束状态列表
    private ArrayList<ArrayList<Integer>> MDFA_AdjacencyTable; //最小化DFA的邻接表


    //初始化
    public dfa()
    {
        //符号表
        SignList = new ArrayList<String>();
        //NFA部分
        DFA_StateList = new ArrayList<String>();
        DFA_StartState = 0;
        DFA_EndList = new ArrayList<Integer>();
        DFA_AdjacencyTable = new ArrayList<ArrayList<Integer>>();
        //MNFA部分
        MDFA_Group = new LinkedList<ArrayList<Integer>>();
        MDFA_StartState = 0;
        MDFA_EndList = new ArrayList<Integer>();
        MDFA_AdjacencyTable = new ArrayList<ArrayList<Integer>>();
    }


    //清除所有数据
    private void clearData()
    {
        //符号表
        SignList.clear();
        //NFA部分
        DFA_StateList.clear();
        DFA_StartState = 0;
        DFA_EndList.clear();
        DFA_AdjacencyTable.clear();
        //MNFA部分
        MDFA_Group.clear();
        MDFA_StartState = 0;
        MDFA_EndList.clear();
        MDFA_AdjacencyTable.clear();
    }



    //读取数据
    public void ReadData(String FilePath) throws Exception
    {
        clearData();

        //打开文件输入流
        File DFAFile = new File(FilePath);
        if(!DFAFile.exists())
        {
            throw new Exception("Input file not found!");
        }

        FileReader DFAReader = new FileReader(DFAFile);
        Scanner DFAInput = new Scanner(DFAReader);

        //读入符号
        int SignCount =  DFAInput.nextInt();
        for(int i = 0; i < SignCount;i++)
        {
            SignList.add( DFAInput.next());
        }
        //读入状态
        int StateCount =  DFAInput.nextInt();
        for(int i = 0; i < StateCount;i++)
        {
            DFA_StateList.add( DFAInput.next());
        }
        //读入开始状态
        DFA_StartState =  DFA_StateList.indexOf( DFAInput.next());
        //读入结束状态
        int EndCount =  DFAInput.nextInt();
        for(int i = 0; i < EndCount; i++)
        {
            DFA_EndList.add( DFA_StateList.indexOf( DFAInput.next()));
        }
        //读入各边数据，构建邻接表
        for(int i = 0; i < StateCount; i++)
        {
            ArrayList<Integer> curLine = new ArrayList<Integer>();
            for(int j = 0; j < SignCount; j++)
            {
                String state = DFAInput.next();
                if(state.equals("-1"))
                {
                    curLine.add(-1);
                }
                else
                {
                    curLine.add(DFA_StateList.indexOf(state));
                }
            }
            DFA_AdjacencyTable.add(curLine);
        }

        DFAInput.close();
        DFAReader.close();
    }


    //进行最小化操作
    public void Minimize() throws Exception
    {
        int StateCount = DFA_StateList.size();
        int SignCount = SignList.size();
        ArrayList<Integer> endGroup = new ArrayList<Integer>(); //接受状态组
        ArrayList<Integer> otherGroup = new ArrayList<Integer>(); //非接受状态组

        for(int i = 0; i < StateCount; i++)
        {
            if(DFA_EndList.contains(i))
            {
                endGroup.add(i);
            }
            else
            {
                otherGroup.add(i);
            }
        }

        MDFA_Group.add(endGroup);
        MDFA_Group.add(otherGroup);

        System.out.println("终状态");
        for(int x : endGroup)
        {
            System.out.print(DFA_StateList.get(x) + " ");
        }
        System.out.println();


        System.out.println("非终状态");
        for(int x : otherGroup)
        {
            System.out.print(DFA_StateList.get(x) + " ");
        }
        System.out.println();
        System.out.println();

        //分组
        boolean split = true;
        while(split)
        {
            split = false;


            System.out.println();//输出当前划分集合
            System.out.println("此时划分集合:");

            for(ArrayList<Integer> group : MDFA_Group) //遍历各分组
            {

                for (int x : group)//输出此时运算的集合
                {
                    System.out.print(DFA_StateList.get(x) + " ");
                }
                System.out.print("\t");
            }
            System.out.println();


            for(ArrayList<Integer> group : MDFA_Group) //遍历各分组
            {

                for(int x : group)//输出此时运算的集合
                {
                    System.out.print(DFA_StateList.get(x) + " ");
                }
                System.out.print(":");
                //System.out.println();

                for(int i = 0; i < SignCount; i++) //对当前分组依次使用各符号处理
                {
                    ArrayList<ArrayList<Integer>>  splitGroup = GroupUp(group, i);
                    if(i!=SignCount-1)
                    {
                        System.out.print("\t");
                    }
                    else
                        System.out.println();
                    if(splitGroup.size() == 1) //未发生分裂(分组内元素仍被划分至同一组)
                    {
                        continue;
                    }
                    else
                    {
                        split = true;
                        //删除旧分组，插入新分组
                        MDFA_Group.remove(group);
                        for(ArrayList<Integer> newGroup : splitGroup)
                        {
                            MDFA_Group.add(newGroup);
                        }
                    }
                    if(split) //发生分裂则跳出循环从头开始处理(下同)
                    {
                        break;
                    }
                }
                if(split)
                {
                    break;
                }
            }
        }

        //为分组创建邻接表
        for(int i = 0; i < MDFA_Group.size(); i++)
        {
            ArrayList<Integer> curLine = new ArrayList<Integer>();
            int stateIndex = MDFA_Group.get(i).get(0);
            int ToIndex = 0;
            for(int j = 0; j < SignList.size(); j++) //依次处理各符号
            {
                ToIndex = DFA_AdjacencyTable.get(stateIndex).get(j);
                if(ToIndex == -1) //分组在该符号上没有转换则存入-1
                {
                    curLine.add(-1);
                }
                else //否则确定分组下标
                {
                    for(int k = 0; k < MDFA_Group.size(); k++)
                    {
                        if(MDFA_Group.get(k).contains(ToIndex))
                        {
                            curLine.add(k);
                        }
                    }
                }
            }
            MDFA_AdjacencyTable.add(curLine);
        }

        //获取开始状态
        for(int i = 0; i < MDFA_Group.size(); i++)
        {
            ArrayList<Integer> curGroup = MDFA_Group.get(i);
            if(curGroup.contains(DFA_StartState))
            {
                MDFA_StartState = i;
                break;
            }
        }

        //获取接受状态
        for(int i = 0; i < MDFA_Group.size(); i++)
        {
            ArrayList<Integer> curGroup = MDFA_Group.get(i);
            for(int endState : DFA_EndList)
            {
                if(curGroup.contains(endState))
                {
                    MDFA_EndList.add(i);
                    break;
                }
            }
        }
    }



    //分组辅助函数，返回按当前符号进行划分的分组
    private ArrayList<ArrayList<Integer>> GroupUp(ArrayList<Integer> curGroup, int signIndex)
    {
        ArrayList<ArrayList<Integer>> groups = new ArrayList<ArrayList<Integer>>(); //分组集合
        ArrayList<Integer> correspondId = new ArrayList<Integer>(); //分组集合中各分组对应的转换到的id

        ArrayList<Integer> ToList= new ArrayList<Integer>();

        for(int x : curGroup)
        {
            int ToStateIndex = DFA_AdjacencyTable.get(x).get(signIndex);
            System.out.print(DFA_StateList.get(ToStateIndex)+" ");
            int ToGroupId = -1;
            //确定各状态的去向
            if(ToStateIndex == -1) //没有相应转换则去向为-1
            {
                ToGroupId = -1;
            }
            else
            {
                for(int i = MDFA_Group.size() - 1; i >= 0; i--) //遍历各分组以确定去向
                {
                    if(MDFA_Group.get(i).contains(ToStateIndex))
                    {
                        ToGroupId = i;
                        break;
                    }
                }
            }
            ToList.add(ToGroupId);
            if(!correspondId.contains(ToGroupId)) //检查去向是否已有
            {
                correspondId.add(ToGroupId);
                groups.add(new ArrayList<Integer>());
            }
        }

        for(int i = 0; i < curGroup.size(); i++) //将状态放入相应去向的分组中
        {
            int index = correspondId.indexOf(ToList.get(i));
            groups.get(index).add(curGroup.get(i));
        }

        return groups;
    }


    //结果输入
    public void ShowResult()
    {
        System.out.println("MDFA State List:");
        for(int i = 0; i < MDFA_Group.size(); i++)
        {
            System.out.print( "StateId: " + i + "\tContainedNFAState: ");
            for(int x : MDFA_Group.get(i))
            {
                System.out.print(DFA_StateList.get(x) + " ");
            }
            System.out.println();
        }

        //输出开始状态集合
        System.out.println("\nStart State: " + MDFA_StartState);

        //输出终结状态集合
        System.out.print("End State List: ");
        for(int x : MDFA_EndList)
        {
            System.out.print(x + " ");
        }
        System.out.println();

        System.out.println("\nAdjacency Relation:");
        System.out.print("Id\t");
        for(String x : SignList)
        {
            System.out.print(x + "\t");
        }
        System.out.println();
        for(int i = 0; i < MDFA_AdjacencyTable.size(); i++)
        {
            System.out.print(i + "\t");
            for(int x : MDFA_AdjacencyTable.get(i))
            {
                System.out.print(x + "\t");
            }
            System.out.println();
        }
    }


    public static void main(String [] args) throws Exception
    {
        dfa mdfa = new dfa();
        mdfa.ReadData("TEMP.txt");
        mdfa.Minimize();
        mdfa.ShowResult();
    }

//    public void ReadData(String FilePath) throws Exception; //从文件中读取数据
//    public void Minimize() throws Exception; //最小化操作
//    public void ShowResult(); //输出结果
//
//    private void clearData(); //清除所有数据
//    private ArrayList<ArrayList<Integer>> GroupUp(ArrayList<Integer> curGroup, int signIndex); //对当前的分组curGroup按照下标为signIndex的输入符号进行分组划分
}
