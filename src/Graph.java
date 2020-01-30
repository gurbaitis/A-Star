import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

//*********************************************************************************************************
//Gabriel Urbaitis
//
// The Graph class uses the strings contained in the Dictionary Hashset to create corresponding Nodes
// and set their edges, all of the words in the Dictionary one Levenshtein operation away.
//*********************************************************************************************************

public class Graph
{
    private final Dictionary DICTIONARY;
    public PriorityQueue pathFinder;
    public HashMap<String, Node> graph;
    char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    public Graph(Dictionary DICTIONARY)
    {
        this.DICTIONARY = DICTIONARY;
        graph = new HashMap<>(DICTIONARY.size());
        makeGraph(DICTIONARY);
    }
    //***********************************
    //@ param String "word"
    // - Any of the words in the Dictionary
    //@return Node
    // Returns the Node corresponding
    // with the requested word.
    //***********************************
    public Node getNode(String word) throws RuntimeException
    {
        if (graph.containsKey(word))
        {
            return graph.get(word);
        }
        else
        {
            throw new RuntimeException("Node does not appear in graph.");
        }
    }
    //***********************************
    // @param Dictionary "dict"
    // - the DICTIONARY from which the
    // graph is made
    // Creates nodes for every String,
    // and fills the edges by creating
    // all possible combinations of
    // Strings one Levenshtein operation
    // away and checking and adding the
    // ones that appear in the DICTIONARY.
    //***********************************
    public void makeGraph(Dictionary dict)
    {
        for (String iterString : dict)
        {
            Node N = new Node(iterString);
            aphbtIns(N);
            aphbtRplc(N);
            deleteChar(N);
            graph.put(iterString, N);
        }
    }
    //***********************************
    // @param Node "N"
    // - The node for which insertions
    // of all 26 letters will be performed
    // at all possible spaces in the word.
    // Called for all words in the
    // Dictionary.
    // Inserts all of the letters of the
    // alphabet into the spaces before and
    // after each letter of the word
    // represented by the node. Adds the
    // combinations appearing in the
    // Dictionary to the Node's edges field.
    //***********************************
    public void aphbtIns(Node N)
    {
        int len = N.name.length();
        for (int h = 0; h <= len; h++)
        {
            for (int i = 0; i < 26; i++)
            {
                StringBuilder sb = new StringBuilder(N.name);
                sb.insert(h, alphabet[i]);
                if (DICTIONARY.Wordset.contains(sb.toString()))
                {
                    if (sb.length() > len)
                    {
                        N.edges.add(sb.toString());
                    }
                    if (sb.length() == len)
                    {
                        N.edges.add(sb.toString());
                    }
                }
            }
        }
    }
    //***********************************
    // @param Node "N"
    // - The node for which replacements
    // of all 26 letters will be performed
    // at all letters in the word.
    // Called for all words in the
    // Dictionary.
    // Replaces all of the letters of the
    // String in the node with the 26 letters
    // of the alphabet. Adds the
    // combinations appearing in the
    // Dictionary to the Node's edges field.
    // Removes edges to itself caused
    // by replacing letters with the same
    // letter.
    //***********************************
    public void aphbtRplc(Node N)
    {
        int len = N.name.length();
        for (int h = 0; h < len; h++)
        {
            for (int i = 0; i < 26; i++)
            {
                StringBuilder sb = new StringBuilder(N.name);
                sb.replace(h, h + 1, String.valueOf(alphabet[i]));
                if (DICTIONARY.Wordset.contains(sb.toString()))
                {
                    if (sb.length() == len)
                    {
                        N.edges.add(sb.toString());
                    }
                }
            }
        }
        N.edges.remove(N.name);
    }
    //***********************************
    // @param Node "N"
    // - The node for which single deletions
    // will be performed at all letters in
    // the word.
    // Called for all words in the
    // Dictionary.
    // Deletes one of all of the letters of the
    // String in the node  Adds the
    // combinations appearing in the
    // Dictionary to the Node's edges field.
    //***********************************
    public void deleteChar(Node N)
    {
        int len = N.name.length();
        for (int h = 0; h < len; h++)
        {
            for (int i = 0; i < 26; i++)
            {
                StringBuilder sb = new StringBuilder(N.name);
                sb.delete(h, h + 1);
                if (DICTIONARY.Wordset.contains(sb.toString()))
                {
                    if (sb.length() == len)
                    {
                        N.edges.add(sb.toString());
                    }
                    if (sb.length() < len)
                    {
                        N.edges.add(sb.toString());
                    }
                }
            }
        }
    }
    //***********************************
    // @param String "string"
    // "string" - input args
    // Performed on all input args
    // @return boolean
    // Whether or not there is a path.
    // Checks to see if the input args have
    // a path to one another
    //***********************************
    public boolean contains(String string)
    {
        return graph.containsKey(string);
    }
    //*********************************************************************************************************
    //Gabriel Urbaitis
    //
    // The Node class is the atomic component of the Dictionary class, with fields for the all of the Strings
    // one Levenshtein operation away, the String itself to be used as Representation, and the priority
    // for calculating the A* shortest path.
    //*********************************************************************************************************
    public class Node
    {
        public String name;
        HashSet<String> edges;
        private int priority;

        public Node(String wordName)
        {
            this.name = wordName;
            this.edges = new HashSet<String>();
        }
        //***********************************
        //@return int
        // The priority of the Node, calculated
        // from combined Levenshtein distances
        // Returns the node's priority.
        //***********************************
        public int getPriority()
        {
            return priority;
        }
        //***********************************
        //@param int "newPrior"
        //The priority value to be assigned to
        // the node.
        // Sets the node's priority
        //***********************************
        public void setPriority(int newPrior)
        {
            priority = newPrior;
        }

    }
}
