package com.travelMaker;

import java.util.ArrayList;

/**
 * Created by yjin on 2016-06-22.
 */
public class KnapSack {
    public ArrayList<Product> exProducts;
    public ArrayList<Product> oriProducts;
    public ArrayList<Integer>  exProductsIdx;


    public KnapSack(ArrayList<Product> products, int maxweight) {
        // define items here
        int pointlimit = 1;
        int tmpval = 1;
        int n = products.size();
        int[] vi=new int[n];
        int[] wi=new int[n];
        // set capacity
        double pointmod = Math.pow(10,pointlimit);
        int W = (int) (maxweight*pointmod);
        oriProducts = products;

        for(int i = 0;i<n;i++)
        {
            vi[i] = tmpval;
            wi[i] = (int) (Double.parseDouble(products.get(i).getWeight())*pointmod);
        }
        // V[i, capacity w] will store the maximum combined value of any subset of items {0,1,...,i} of combined size at most w.
        int[][] V = new int[n][W+1];
        // keep[i, capacity w] is true when item i is part of an optimal solution to the sub-problem which has to choose
        // from {0-i} items when capacity is w; that's the reason why it is a dynamic programming algorithm; the solution
        // to the original problem has to be recovered going back from the full problem (i.e., from keep[n-1, W]) to sub-problems;
        boolean[][] keep = new boolean[n][W+1];
        for ( int i = 0; i < n; i++ ) {
            for ( int w=0; w <= W; w++ ) {
                keep[i][w] = false;
            }
        }

        // the main algorithm starts here

        // (1) first compute maximum value for all w < W when only first item 0 can be taken, i.e., we consider the
        //     first sub-problem when only the first item can be taken;
        for ( int w = 0; w <= W; w++ ) {
            if ( wi[0] <= w) {
                V[0][w] = wi[0];
                keep[0][w] = true;	// set true, which means that item 0 can be taken when left capacity is exactly w
            } else {
                V[0][w] = 0;
            }
        }
        //    printV(V, "maximum value V, for all w<W, when only item 0 can be taken");
        //    printkeep(keep, "true for values w < W for which item 0 can be taken", 0);

        // (2) then compute maximum value for all w < W when any subset of items 0-i can be taken
        for ( int i = 1; i < n; i++ ) {
            // for every i, we solve a sub-problem where we choose from {0-i} for every capacity w
            for ( int w = 0; w <= W; w++) {
                if ( wi[i] <= w && vi[i] + V[i-1][w-wi[i]] > V[i-1][w] ) {
                    // take item i, at capacity w, because the value of i plus the value of the best previous subset under
                    // capacity (w-wi[i]) is better then the value of the previous subset under current w; at this
                    // point we know that considering all subsets of {0-i}, i should be taken when w is the capacity.
                    // we do not know the subset of {0-(i-1)} but it can be recovered from keep using backward traversal
                    // on i because subsets were improved when i was growing;
                    V[i][w] = vi[i] + V[i-1][w-wi[i]];
                    // set true which means that item i belongs to the optimal subset of items {0-i} when capacity that is left is exactly w
                    keep[i][w] = true;
                } else {
                    V[i][w] = V[i-1][w];
                }
            }
        }

        // print the result
        //  System.out.println("Selected items (selected in reverse order):");
        int K = W;
        int wsel = 0;
        int vsel = 0;
        // need to go in the reverse order; what happens here is that given K and i, we have to check if i is part of
        // an optimal subset given capacity K, if yes, we take i, and we continue recovering the optimal subset by
        // reducing K (which means switching to a smaller sub-problem) and finding the next element for reduced capacity
        exProducts = new ArrayList<Product>();
        exProductsIdx = new ArrayList<Integer>();
        for ( int i = n - 1 ; i >= 0; i-- ) {
            if ( keep[i][K] == true) {
                //   System.out.println(i + "\tv=" + vi[i] + "\tw=" + wi[i]);
                wsel += wi[i];
                vsel += vi[i];
                K = K - wi[i];
            }
            else
            {
                exProducts.add(oriProducts.get(i));
                exProductsIdx.add(i);
            }
        }
    }
    public int[] getExProductListIdx(){
        // return exProductsIdx.toArray();
        int[] ret = new int[exProductsIdx.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = exProductsIdx.get(i).intValue();
        }
        return ret;
    }
}
