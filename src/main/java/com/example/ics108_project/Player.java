package com.example.ics108_project;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Scanner;

public class Player {
    private static int score;
    private static final File top5Score = getScoresFile();

    final private static int[] top5ScoresArray = getUpToDateScoresArray();

    /**
     * Method to add a score to the player's current score. This
     * is called when a player successfully clicks on a {@code FallingEntity}
     * @param score the score to be added
     */
    public static void addScore(int score) {
        Player.score += score;
    }

    /**
     * Getter method to get the current score of the player
     * @return the current score of the player
     */
    public static int getScore() {
        return score;
    }

    /**
     * Method that resets the player's score to 0. This should be called
     * after a collision is detected between a {@code FallingEntity} and
     * the {@code floor}
     */
    public static void resetScore() {
        score = 0;
    }

    /**
     * Method that updates the final score of the player after collision.
     * This method updates the top 5 scores in the local memory and the
     * file by calling {@code updateFile()}
     */
    public static void updateFinalScore() {
        boolean updated = false;
        for (int pos = 0; pos < top5ScoresArray.length && !updated; pos++)
            if (score > top5ScoresArray[pos]) {
                shiftScores(pos);
                top5ScoresArray[pos] = score;
                updated = true;
            }
        updateFile();
    }

    /**
     * Method that shifts the ranking of the top scores down if
     * a better score is achieved.
     * @param position the position of the new score
     */
    private static void shiftScores(int position)
    {
        for(int i = top5ScoresArray.length - 1; i > position; i--)
            top5ScoresArray[i] = top5ScoresArray[i - 1];
    }

    /**
     * Method that updates the file storing the top 5 scores with the
     * new rankings of top 5 scores.
     */
    private static void updateFile()
    {
        try {
            assert top5Score != null;
            try(PrintWriter scoreUpdater = new PrintWriter(top5Score))
            {
                for(int num : top5ScoresArray)
                    scoreUpdater.println(num);
            }

        }

        catch(FileNotFoundException exception)
        {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Method that gets the up-to-date scores from the text file and stores it in
     * memory via an array.
     * @return an array containing the up-to-date top 5 scores. The scores are
     *         organized in descending order.
     */
    private static int[] getUpToDateScoresArray()
    {
        int[] scoresArray = new int[5];
        Scanner scanner = scoreFileScanner();
        assert scanner != null;

        int pos = 0;
        while(scanner.hasNext())
            scoresArray[pos++] = scanner.nextInt();

        return scoresArray;
    }

    /**
     * Method to get a {@code Scanner} of the top 5 scores text file. This
     * is called when wanting to read the file.
     * @return a {@code Scanner} object that reads from {@code top5scores.txt}.
     *         If the file was not found, {@code null} is returned, but this
     *         should never happen.
     */
    public static Scanner scoreFileScanner()
    {
       try
       {
           assert top5Score != null;
           return new Scanner(top5Score);
       }
        catch(FileNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    /**
     * Method that clears the stored information about the top 5 scores.
     * This will reset all the scores back to 0.
     */
    public static void clearData()
    {
        try {
            assert top5Score != null;
            try(PrintWriter printWriter = new PrintWriter(top5Score))
            {
                for(int i = 0; i < 5; i++)
                {
                    printWriter.println(0);
                    top5ScoresArray[i] = 0;
                }
            }
        }
        catch(FileNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Method to get the {@code top5scores.txt} file. If the file or directory does not
     * exist, they are created with the default value of the file being {@code 0 0 0 0 0}
     * each stored in its separate line. The file is stored at
     * {@code Documents/My Games/Ap-Fall-E/top5scores.txt}.
     * @return a {@code File} object that points to {@code Documents/My Games/Ap-Fall-E/top5scores.txt}
     */
    private static File getScoresFile() {
        String documentsPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()
                + "/My Games/Ap-Fall-E";
        File dir = new File(documentsPath);
        if (!dir.exists())
            if (!dir.mkdirs())
                return null;
        File file = new File(documentsPath + "/top5scores.txt");

        try {
            if (!file.exists()) {
                if (!file.createNewFile())
                    return null;


                // Writes the default values for the file
                PrintWriter pw = new PrintWriter(file);
                for (int i=0; i<5; i++)
                    pw.println(0);
                pw.close();
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return file;
    }
}
