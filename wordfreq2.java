import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
* Tester class for RBTree. 
* @author Lukas Edman
**/
class wordfreq2 {
	public static void main(String[] args){

		if (args.length == 0){
			System.out.println("Must include a filename.");
			System.exit(0);
		} else if (args.length > 1){
			System.out.println("Must include a valid filename as a single argument.");
			System.exit(0);
		} else {

			Scanner sc = null;
			try{
				sc = new Scanner(new File(args[0]));
			} catch (FileNotFoundException e){
				System.out.println("File not found.");
				System.exit(0);
			}

			RBTree<String,Integer> tree = new RBTree<String, Integer>();

			sc.useDelimiter("[^A-Za-z0-9'_]+");
			String word;
			while(sc.hasNext()){
				word = sc.next().toLowerCase();
				int wlen = word.length();
				while (wlen > 0 && word.charAt(wlen-1)=='\'') { 
					word = word.substring(0,wlen-1); 
					wlen--; 
				}
				while (wlen > 0 && word.charAt(0)=='\'') { 
					word = word.substring(1); 
					wlen--; 
				}
				if (wlen > 0){
					Integer x = tree.get(word);
					if (x== null || x== 0) tree.put(word,1);
					else tree.put(word,x.intValue()+1);
				}
			}

			System.out.println("This text contains "+ tree.size() +" distinct words.");
			System.out.println("Please enter a word to get its frequency, or hit enter to leave.");

			Scanner sc2 = new Scanner(System.in);
			while(true){
				String input = sc2.nextLine();
				if (input.length()==0) {
					System.out.println("Goodbye!");
					return;
				} else if (input.charAt(0)=='-'){
					input = input.substring(1);
					tree.delete(input);
					System.out.println("\"" + input + "\" has been deleted.");
				} else if (input.charAt(0)=='<'){
					if (input.length()==1){
						System.out.println("The alphabetically-first word in the text is \"" + tree.getMinKey() + "\".");
					} else {
						input = input.substring(1);
						String x = tree.findPredecessor(input);
						if (x==null) System.out.println("\"" + input + "\" is the first word alphabetically.");
						else System.out.println("The word \"" + x + "\" comes before \"" + input + "\".");
					}
				} else if (input.charAt(0)=='>'){
					if (input.length()==1){
						System.out.println("The alphabetically-last word in the text is \"" + tree.getMaxKey() + "\".");
					} else {
						input = input.substring(1);
						String x = tree.findSuccessor(input);
						if (x==null) System.out.println("\"" + input + "\" is the last word alphabetically.");
						else System.out.println("The word \"" + x + "\" comes after \"" + input + "\".");
					}
				} else {
					int x = tree.get(input)==null ? 0 : tree.get(input);
					if (x == 0) System.out.println("\"" + input + "\" does not appear."); 
					else System.out.println("\"" + input + "\" appears " + x + " times.");
				}
			}
		}
	}

}
