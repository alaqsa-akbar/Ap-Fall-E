package com.example.ics108_project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Player {
    private static int score;
    private static final File top5Score = new File("src\\main\\resources\\top5scores.txt");
    final private static int[] top5ScoresArray = upToDateArray();

    public static void addScore(int score) {
        Player.score += score;
    }

    public static int getScore() {
        return score;
    }

    public static void resetScore() {
        score = 0;
    }
    public static void getFinalScore() {
        boolean updated = false;
        for (int pos = 0; pos < top5ScoresArray.length && !updated; pos++)
            if (score > top5ScoresArray[pos]) {
                shiftScores(pos);
                top5ScoresArray[pos] = score;
                updated = true;

            }
        updateFile();

    }
    private static void shiftScores(int position)
    {
        for(int i = top5ScoresArray.length - 1; i > position; i--)
            top5ScoresArray[i] = top5ScoresArray[i - 1];
    }
    private static void updateFile()
    {
        try(PrintWriter scoreUpdater = new PrintWriter(top5Score))
        {
            for(int num : top5ScoresArray)
                scoreUpdater.println(num);
        }

        catch(FileNotFoundException exception)
        {
            System.out.println(exception.getMessage());
        }
    }
    private static int[] upToDateArray()
    {
        int[] scoresArray = new int[5];
        Scanner scanner = scoreFileScanner();
        assert scanner != null;

        int pos = 0;
        while(scanner.hasNext())
            scoresArray[pos++] = scanner.nextInt();

        return scoresArray;
    }
    static Scanner scoreFileScanner()
    {
       try
       {
           return new Scanner(top5Score);
       }
        catch(FileNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }

        return null;
    }
    static void clearData()
    {
        try(PrintWriter printWriter = new PrintWriter(top5Score))
        {
            for(int i = 0; i < 5; i++)
                printWriter.println(0);
        }
        catch(FileNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}

