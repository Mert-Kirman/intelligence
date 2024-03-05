import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        // Take the name of the input file from the terminal
        File input = new File(args[0]);
        Scanner scanner = new Scanner(input);
        // Take the name of the output file from the terminal
        FileWriter fWriter = new FileWriter(args[1]);

        // Create the family AVL Tree
        AvlTree family = new AvlTree(fWriter);

        // Insert the boss to the AVL Tree
        String[] boss = scanner.nextLine().split(" ");
        String name = boss[0];
        double GMS = Double.parseDouble(boss[1]);
        family.insert(name, GMS);

        // Store all the remaining lines in the input file to an arraylist of arrays for future processing
        List<String[]> inputArr = new ArrayList<>();
        while(scanner.hasNextLine()) {
            inputArr.add(scanner.nextLine().split(" "));
        }
        scanner.close();

        // Process tips and analysis requests accordingly
        for(String[] tips:inputArr) {
            if(tips[0].equals("MEMBER_IN")) {
                name = tips[1];
                GMS = Double.parseDouble(tips[2]);
                family.insert(name, GMS);
            }
            else if(tips[0].equals("MEMBER_OUT")) {
                GMS = Double.parseDouble(tips[2]);
                family.remove(GMS);
            }
            else if(tips[0].equals("INTEL_TARGET")) {
                double GMS1 = Double.parseDouble(tips[2]);
                double GMS2 = Double.parseDouble(tips[4]);
                family.findSuperior(GMS1, GMS2);
            }
            else if(tips[0].equals("INTEL_DIVIDE")) {
                family.divide();
            }
            else if(tips[0].equals("INTEL_RANK")) {
                GMS = Double.parseDouble(tips[2]);
                family.monitorRank(GMS);
            }
        }

        fWriter.close();
    }
}