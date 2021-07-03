public class WhatDoesItLookLike
{
    public static void main (String[]args)
    {
    	System.out.println(presi());
        System.out.println(candy());
    }

    public static String candy()
    {
        Candidate.loadData("TX");
        String [][] cand = Candidate.getAllCandStats(2);
        return TestDriver_Liam.formatPrint(cand);
    }

    public static String presi()
    {

        Voter.loadData();
        Ballot.loadData("TX");
        Candidate.loadData("TX");

        String [][] cand = Candidate.getAllPresStats();
        return TestDriver_Liam.formatPrint(cand);
    }

}