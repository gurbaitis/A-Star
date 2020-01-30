import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

//*********************************************************************************************************
//Gabriel Urbaitis
//
// The Dictionary Class uses Java 8's Buffered reader to read in and delimit the input text file into a
// Hashset on which all comparative operations will be performed.
//
//*********************************************************************************************************
public class Dictionary implements Iterable<String>
{
    public HashSet<String> Wordset;
    public BufferedReader openFile;
    public String SOURCE; // File path for dictionary input


    public Dictionary(String path)
    {
        SOURCE = path;
        Wordset = new HashSet<>();
        prepare();
    }
    //***********************************
    // Reads and delimits the Dictionary
    // file into a Hashset of individual
    // Strings
    //***********************************
    private void prepare()
    {
        File file = new File(SOURCE);
        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String input;
            while ((input = reader.readLine()) != null)

            {
                Wordset.add(input);
            }
        } catch (IOException e)
        {
            System.exit(-1);
        }

    }
    //***********************************
    //@ return int
    //Returns the number of Strings in
    //the Dictionary
    //***********************************
    public int size()
    {
        return Wordset.size();
    }

    @Override
    public Iterator<String> iterator()
    {
        return Wordset.iterator();
    }
}
