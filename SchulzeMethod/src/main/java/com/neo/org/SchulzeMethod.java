package com.neo.org;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author doubi.liu
 * @version V1.0
 * @Title: SchulzeMethod
 * @Package com.neo.org
 * @Description: (用一句话描述该文件做什么)
 * @date Created in 17:31 2019/7/8
 */
public class SchulzeMethod {
    public static void main(String[] args) {
        Map<String,Integer> votes=new HashMap<String, Integer>();
        votes.put("0,2,1,4,3",5);
        votes.put("0,3,4,2,1",5);
        votes.put("1,4,3,0,2",8);
        votes.put("2,0,1,4,3",3);
        votes.put("2,0,4,1,3",7);
        votes.put("2,1,0,3,4",2);
        votes.put("3,2,4,1,0",7);
        votes.put("4,1,0,3,2",8);

        Map<List<String>,Integer> tempVotes=new HashMap();
        for (Map.Entry<String,Integer> e:votes.entrySet()){
            String[] arr = e.getKey().split(",");
            List<String> list = Arrays.asList(arr);
            tempVotes.put(list,e.getValue());
        }


        List<String> nodelist = new ArrayList<String>();
        nodelist.add("0");
        nodelist.add("1");
        nodelist.add("2");
        nodelist.add("3");
        nodelist.add("4");


        int[][] dArray=new int[nodelist.size()][nodelist.size()];

        for (int i=0;i<nodelist.size();i++){
            for (int j=0;j<nodelist.size();j++){
                for (Map.Entry<List<String>,Integer> e:tempVotes.entrySet()){
                    List<String> key=e.getKey();
                    if (key.indexOf(String.valueOf(i))<key.indexOf(String.valueOf(j))){
                        dArray[i][j]+=e.getValue();
                    }
                }
            }
        }
        SchulzeMethod sMethod=new SchulzeMethod();
        Map<String, Integer> vGraphic=sMethod.convertDArrayToVGraphic(dArray);
        FindAllPathMethod fMethod=new FindAllPathMethod(nodelist,vGraphic);
        List<List<String>> paths=fMethod.findAllPath();
        int[][] pArray=sMethod.creatPArray(nodelist,paths,vGraphic);
        sMethod.printPArray(pArray,nodelist);




    }

    /*
     *@Description:将偏爱程度数组转换成节点矢量关系
     *@Param:  [dArray] 偏爱数组
     *@Return: java.util.Map<java.lang.String,java.lang.Integer> vectorGraphic 节点矢量关系
     *@Date:   2019/7/9
     *@Author: doubi.liu
     */
    public Map<String, Integer> convertDArrayToVGraphic(int[][] dArray) {
        Map<String, Integer> vectorGraphic = new HashMap<String, Integer>();
        for (int i = 0; i < dArray.length; i++) {
            for (int j = 0; j < i; j++) {
                if (dArray[i][j] > dArray[j][i]) {
                    vectorGraphic.put(Integer.toString(i) + "," + Integer.toString(j), dArray[i][j]);
                } else if (dArray[i][j] == dArray[j][i]) {
                    vectorGraphic.put(Integer.toString(i) + "," + Integer.toString(j), dArray[i][j]);
                    vectorGraphic.put(Integer.toString(j) + "," + Integer.toString(i), dArray[j][i]);
                } else {
                    vectorGraphic.put(Integer.toString(j) + "," + Integer.toString(i), dArray[j][i]);
                }
            }
        }
        return vectorGraphic;
    }

    public int[][] creatPArray(List<String> nodelist, List<List<String>> paths, Map<String, Integer> vectorGraphic) {

        int[][] pArray = new int[nodelist.size()][nodelist.size()];
        for (int i = 0; i < nodelist.size(); i++) {
            for (int j = 0; j < nodelist.size(); j++) {
                pArray[i][j] = -1;
            }
        }
        Map<String, Integer> pathWeights = new HashMap<String, Integer>();
        for (List<String> path : paths) {
            int pathWeight = -1;
            String pathName = path.get(0) + "," + path.get(path.size() - 1);
            for (int i = 0; i < path.size() - 1; i++) {
                int weight = vectorGraphic.get(path.get(i) + "," + path.get(i + 1));
                if (pathWeight < 0 || pathWeight > weight) {
                    pathWeight = weight;
                }
            }
            if (pathWeights.containsKey(pathName)) {
                if (pathWeights.get(pathName) < pathWeight) {
                    pathWeights.put(pathName, pathWeight);
                }
            } else {
                pathWeights.put(pathName, pathWeight);
            }

        }
        for (Map.Entry<String, Integer> e : pathWeights.entrySet()) {
            String[] temp = e.getKey().split(",");
            int startNodeIndex = Integer.parseInt(temp[0]);
            int endNodeIndex = Integer.parseInt(temp[1]);
            pArray[startNodeIndex][endNodeIndex] = e.getValue();
        }
        return pArray;
    }

    /**
     * @return void
     * @Description: 打印邻接矩阵
     * @param: null
     * @date: 2019/7/8
     * @author:doubi.liu
     */
    public  void printPArray(int[][] pArray,List<String> nodesList) {
        System.out.println("pArray:行代表from,列代表to");
        System.out.print("From/to ");
        nodesList.forEach(p -> System.out.print(p + "   "));
        System.out.print("\n");
        for (int i = 0; i < pArray.length; i++) {
            System.out.print(nodesList.get(i) + "      ");
            for (int j = 0; j < pArray[i].length; j++) {
                System.out.print(pArray[i][j] > 0 ? pArray[i][j] + "   " : pArray[i][j] + "  ");
            }
            System.out.print("\n");
        }
    }
}