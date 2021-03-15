package cn.team.min_dfa.Controller;

import javax.management.relation.Relation;
import java.util.ArrayList;

public class Result {
    public ArrayList<ArrayList<ArrayList<Character>>> TripleList;
    public ArrayList<Integer> StateListNum;
    public ArrayList<ArrayList<Character>> StateListChar;
    public int StartStatic;
    public ArrayList<Integer> EndStatic;
    public ArrayList<ArrayList<Integer>> AdjacencyRelation;

    public Result()
    {
        TripleList = new ArrayList<>();
        StartStatic =0;
        StateListChar = new ArrayList<>();
        StateListNum = new ArrayList<>();
        EndStatic =new ArrayList<>();
        AdjacencyRelation = new ArrayList<>();
    }
}
