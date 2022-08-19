package it.oberwalder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.SATURDAY;

public class MessageToBanner {

    public static void execute() {
        BufferedImage img = imageFromBmp();
       List numberOfCommitsInOrder = listFromRaster(img);
       LocalDate startDate = getGithubMatrixLastPoint(LocalDate.now());
       commitsFromDateAndList(startDate, numberOfCommitsInOrder);
    }

    private static List listFromRaster(BufferedImage img) {
        List<Integer> listOfPixels = new ArrayList<>();
        int [] dataBuffer = new int[10];
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int pixelRGB = img.getRGB(i, j);
                int red = (pixelRGB >> 16) & 255;
                listOfPixels.add(red);
            }
        }
        Collections.reverse(listOfPixels);
        List<Integer> listOfRedValuesHalfAmount = listOfPixels.stream().map(x -> (255-x)/2).collect(Collectors.toList());
        return listOfRedValuesHalfAmount;
    }

    private static void commitsFromDateAndList(LocalDate startDate, List<Integer> list) {
        LocalDate workingDate = LocalDate.from(startDate);
        for (Integer integer : list) {
            gitCommit(integer, workingDate);
            workingDate = workingDate.minusDays(1);
        }

    }

    private static void gitCommit(int value, LocalDate workingDate) {
        try {
            for (int i = 0; i < value; i++) {
                ProcessBuilder pb = new ProcessBuilder("./test.sh", workingDate.toString());
                Map<String, String> env = pb.environment();
                pb.directory(new File("/home/work/tools/git/lbanner/"));
                Process p = pb.start();
            }
        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
        }
    }

    private static LocalDate getGithubMatrixLastPoint(LocalDate localDate) {
    return localDate
            .minusWeeks(2)
            .with(SATURDAY);
    }

    private static BufferedImage imageFromBmp() {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("/home/work/tools/git/lbanner/src/main/resources/topright.bmp"));
        } catch(Exception ignored) {
            System.out.println( ignored.getMessage());
        }
       return img;
    }
}
