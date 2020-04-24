package com.study.studymarket.utils;

public class BuildTree {
    public TreeNode reConstructBinaryTree(int [] pre,int [] in) {
        TreeNode root=ConstructCore(pre,0,pre.length-1,in,0,in.length-1);
        return root;
    }

    /**
     *
     * @param pre
     * @param startPre 前序数组的下标开始位置
     * @param endPre 前序数组的下标结束位置
     * @param in
     * @param startIn 中序数组的下标结束位置
     * @param endIn 中序数组的下标结束位置
     * @return
     */
    public TreeNode ConstructCore(int[] pre,int startPre,int endPre,int[] in,int startIn,int endIn)
    {
        if(startPre>endPre||startIn>endIn)
            return null;
        TreeNode node = new TreeNode(pre[startPre]);
        for(int i=startIn;i<=endIn;i++)
        {
            // 如果找到根结点，就确定子树的范围接着拆，if(in[i]==pre[startPre])这段代码就是找根节点
            if(in[i]==pre[startPre])
            {
                node.left = ConstructCore(pre,startPre+1,startPre+i-startIn,in,startIn,i-1);
                node.right =ConstructCore(pre,startPre+i-startIn+1,endPre,in,i+1,endIn);
                break;
            }
        }
        return node;

    }

    public static void main(String[] args) {
        int[] preArr ;
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}


