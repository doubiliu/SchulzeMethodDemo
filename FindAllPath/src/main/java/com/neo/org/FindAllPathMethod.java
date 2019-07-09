package com.neo.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author doubi.liu
 * @version V1.0
 * @Title: FindAllPathMethod
 * @Package com.neo.org
 * @Description: (用一句话描述该文件做什么)
 * @date Created in 17:17 2019/7/7
 */
public class FindAllPathMethod {


    /*          A --------B
                |         |
                |         |
                |         |
                |         |
                |         |
                C---------D----------E

                Path:
                AB    A->B
                AC    A->C
                AD    A->B->D  A->C->D
                AE    A->B->D->E  A->C->D->E
                BD    B->D
                BE    B->D->E
                CD    C->D
                CE    C->D->E
                DE    D->E

                ABCDE 对应 01234



    */
    //节点
    private List<String> nodesList = new ArrayList<String>();
    //有向图
    private  Map<String, Integer> vectorGraphic = new HashMap<String, Integer>();
    //邻接矩阵
    private  int[][] edgeArray;
    //路径
    private  List<List<String>> path = new ArrayList<>();


    /*
     *@Description:构造函数
     *@Param: nodesList 节点集合
     *@Param: vectorGraphic 节点有向图连通性
     *@Date:   2019/7/9
     *@Author: doubi.liu
     */
    public FindAllPathMethod(List<String> nodesList, Map<String, Integer> vectorGraphic) {
        this.nodesList = nodesList;
        this.vectorGraphic = vectorGraphic;
        createEdgeArray();
    }

    /**
     * @return void
     * @Description:创建邻接矩阵,两个节点之间存在连通性则权重为正数，否则为负数
     * @param: null
     * @date: 2019/7/8
     * @author:doubi.liu
     */
    public  void createEdgeArray() {
        int nodeCount = nodesList.size();
        edgeArray = new int[nodeCount][nodeCount];
        for (int i = 0; i < nodeCount; i++)
            for (int j = 0; j < nodeCount; j++) {
                edgeArray[i][j] = -1;
            }
        for (Map.Entry<String, Integer> entry : vectorGraphic.entrySet()) {

            String[] temp=entry.getKey().split(",");

            String startNode = temp[0];
            int startNodeIndex = Integer.parseInt(temp[0]);
            String endNode = temp[1];
            int endNodeIndex = Integer.parseInt(temp[1]);
            int weight = entry.getValue();
            edgeArray[startNodeIndex][endNodeIndex] = weight;
        }
    }

    /**
     * @return void
     * @Description:创建startNode到endNode的所有路径
     * @param: startNode 起始节点
     * @param: endNode   终止节点
     * @date: 2019/7/8
     * @author:doubi.liu
     */
    public  List<List<String>> createPath(String startNode, String endNode) {
        path.clear();
        List<String> tempUnUsedNodes = new ArrayList<>();
        List<String> tempPath = new ArrayList<>();
        tempUnUsedNodes.addAll(nodesList);
        tempUnUsedNodes.remove(startNode);
        createPath(tempUnUsedNodes, startNode, endNode, tempPath);
        return path;
    }

    /**
     * @Description:创建currentNode到endNode的所有路径
     * @param:unUsedNodes 未使用节点集合
     * @param:currentNode 当前节点
     * @param:endNode     终止节点
     * @param:hasCreatedPath 已创建路径
     * @return void
     * @date: 2019/7/8
     * @author:doubi.liu
     */
    public  void createPath(List<String> unUsedNodes, String currentNode, String endNode,
                                  List<String> hasCreatedPath) {
        if (currentNode.equals(endNode)) {
            List<String> tempPath = new ArrayList<>();
            tempPath.addAll(hasCreatedPath);
            tempPath.add(currentNode);
            path.add(tempPath);
            return;
        }
        //计算下一步所有可以达到的点
        int startNodeIndex = Integer.parseInt(currentNode);
        List<String> achievedNodes = new ArrayList<>();
        for (String unUsedNode : unUsedNodes) {
            int achievedNodeIndex = Integer.parseInt(unUsedNode);
            if (edgeArray[startNodeIndex][achievedNodeIndex] > 0) {
                achievedNodes.add(unUsedNode);
            }
        }
        //退出条件，已达终点
        if (achievedNodes.size() == 0) {
            return;
        } else {
            //继续遍历节点
            for (String achievedNode : achievedNodes) {
                List<String> tempUnUsedNodes = new ArrayList<>();
                tempUnUsedNodes.addAll(unUsedNodes);
                tempUnUsedNodes.remove(achievedNode);
                List<String> tempPath = new ArrayList<>();
                tempPath.addAll(hasCreatedPath);
                tempPath.add(currentNode);
                createPath(tempUnUsedNodes, achievedNode, endNode, tempPath);
            }
        }

    }

    /**
     * @Description:查询所有路径
     * @param:null
     * @return void
     * @date: 2019/7/8
     * @author:doubi.liu
     */
    public  List<List<String>> findAllPath(){
        path.clear();
        for (String i:nodesList) {
            for (String j : nodesList) {
                if (!i.equals(j)) {
                    List<String> tempUnUsedNodes = new ArrayList<>();
                    List<String> tempPath = new ArrayList<>();
                    tempUnUsedNodes.addAll(nodesList);
                    tempUnUsedNodes.remove(i);
                    createPath(tempUnUsedNodes, i, j, tempPath);
                }
            }
        }
        return path;
    }


    /**
     * @return void
     * @Description: 打印邻接矩阵
     * @param: null
     * @date: 2019/7/8
     * @author:doubi.liu
     */
    public  void printEdgeArray() {
        System.out.println("EdgeArray:行代表from,列代表to");
        System.out.print("From/to ");
        nodesList.forEach(p -> System.out.print(p + "   "));
        System.out.print("\n");
        for (int i = 0; i < edgeArray.length; i++) {
            System.out.print(nodesList.get(i) + "      ");
            for (int j = 0; j < edgeArray[i].length; j++) {
                System.out.print(edgeArray[i][j] > 0 ? edgeArray[i][j] + "   " : edgeArray[i][j] + "  ");
            }
            System.out.print("\n");
        }
    }

    /**
     * @Description:
     * @param: 打印从startNode到endNode的所有路径
     * @return void
     * @date: 2019/7/8
     * @author:doubi.liu
     */
    public  void printPath(String startNode, String endNode) {
        System.out.println(String.format("创建路径：{%s}--->{%s}", startNode, endNode));
        createPath(startNode, endNode);
        if (path.size() == 0) {
            System.out.println("此路不通");
            return;
        }
        for (List<String> entry : path) {
            System.out.print("Path:");
            for (String node : entry) {
                System.out.print(node+",");
            }
            System.out.print("\n");
        }
    }


    /**
     * @Description:打印所有路径
     * @param: nul
     * @return void
     * @date: 2019/7/8
     * @author:doubi.liu
     */
    public  void printAllPath() {
        findAllPath();
        for (List<String> entry : path) {
            System.out.print("Path:");
            for (String node : entry) {
                System.out.print(node+",");
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args) {
        List<String> tempNodesList=new ArrayList<>();
        tempNodesList.add("0");
        tempNodesList.add("1");
        tempNodesList.add("2");
        tempNodesList.add("3");
        tempNodesList.add("4");


        Map<String, Integer> tempVectorGraphic=new HashMap<>();
        tempVectorGraphic.put("0,1", 1);
        tempVectorGraphic.put("0,2", 1);
        tempVectorGraphic.put("1,3", 1);
        tempVectorGraphic.put("2,3", 1);
        tempVectorGraphic.put("3,4", 1);


        FindAllPathMethod method=new FindAllPathMethod(tempNodesList,tempVectorGraphic);

        method.printEdgeArray();
        //method.printAllPath();
        method.printPath("0", "4");
        method.printPath("1", "0");
    }
}