//*********************************************************************************************************
//Gabriel Urbaitis
//
// The AStar Class contains all of the operations necessary for calculating the Shortest path between
// two words, including handling variable amounts of input arguments, appropriately designating the
// start and goal words of each pair, calculating Levenshtein distance between the start word's
// neighbor as well as each successive neighbor and the goal word and finally properly displaying
// the shortest paths of each pair of arguments.
//*********************************************************************************************************
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class AStar
{

    private final Graph GRAPH;
    private Graph.Node startWord;
    private Graph.Node endWord;

    public AStar(Graph graph)
    {
        GRAPH = graph;
    }
    //***********************************
    // @param String[] "args"
    // Any number of command line arguments
    // consisting of the file path and
    // start and end words
    // Prints an error message if no
    // arguments are specified, otherwise
    // performs the A* shortest path on the
    // words in the arguments using the
    // dictionary in the filepath.
    //***********************************
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("Please specify some arguments.");
            System.exit(-1);
        }
        new AStar(new Graph(new Dictionary(args[0]))).processArgs(args);
    }
    //***********************************
    //@param int "a","b","c"
    // Any three integers
    // whose min must be evaluated.
    //@return int
    // The min of the three ints.
    // Returns the minimum of the the
    // three inputted integers.
    // Employs the Math "min" argument twice.
    //***********************************
    public static int minThree(int a, int b, int c)
    {
        return Math.min(Math.min(a, b), c);
    }
    //***********************************
    // @param String[] "args"
    // Any number of command line arguments
    // consisting of the file path and
    // start and end words
    // Interprets the command line arguments
    // and performs the A* algorithm on each.
    // Prints error messages for incorrect
    // input.
    //***********************************
    private void processArgs(String[] args)
    {
        int argLength = args.length - 1;
        if (argLength == 0)
        {
            System.out.println("No word pairs given - program terminating.");
            System.exit(-1);
        }
        else if ((argLength) % 2 != 0)
        {
            System.out.println("Invalid number of arguments!");
            System.exit(-1);
        }
        for (int i = 1; i < argLength; i += 2)
        {
            if (!GRAPH.contains(args[i]) ^ !GRAPH.contains(args[i + 1]))
            {
                if (!GRAPH.contains(args[i]))
                {
                    System.out.println(args[i] + " is not in the dictionary.");
                }
                else
                {
                    System.out.println(args[i + 1] + " is not in the dictionary.");
                }
            }
            else if (!GRAPH.contains(args[i]) && !GRAPH.contains(args[i + 1]))
            {
                System.out.println(args[i] + " and " + args[i + 1] + " are not in the dictionary.");
            }
            else
            {
                startWord = GRAPH.getNode(args[i]);
                endWord = GRAPH.getNode(args[i + 1]);
                findPath(startWord, endWord);
            }
        }
    }
    //***********************************
    // @param Strings "start, "goal"
    // Start and end words specified in the
    // command line args.
    // @return int
    // The number of levenshtein operations
    // required to transform one word into
    // the other using the dictionary.
    // Uses a Levenshtein matrix to calculate
    // the number of one character alterations
    // required to transform one String into
    // another.
    //https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance
    //***********************************
    public int levenshtein(String start, String goal)
    {
        int n = start.length();
        int m = goal.length();
        if (n == 0)
        {
            return m;
        }
        if (m == 0)
        {
            return n;
        }
        int[][] lMatrix = new int[n + 1][m + 1];
        for (int g = 0; g < m + 1; g++)
        {
            lMatrix[0][g] = g;
        }
        for (int h = 0; h < n + 1; h++)
        {
            lMatrix[h][0] = h;
        }
        for (int i = 1; i < n + 1; i++)
        {
            for (int j = 1; j < m + 1; j++)
            {
                lMatrix[i][j] = minThree(
                        lMatrix[i - 1][j] + 1,
                        lMatrix[i][j - 1] + 1,
                        lMatrix[i - 1][j - 1] + ((start.charAt(i - 1) == goal.charAt(j - 1)) ? 0 : 1));

            }

        }
        return lMatrix[n][m];
    }
    //***********************************
    //@param Nodes "start", "goal"
    //Start and goal nodes for which the
    // A* algorithm finds the shortest path
    // between.
    // Uses Linked Hashmaps holding the
    // current costs and previously visited
    // nodes as well as the Levenshtein distance
    // to perform the A* algorithm, visiting nodes
    // in order of increasing Levenshtein distance
    // and printing the shortest path.
    // Algorithm: A*
    //Inspired by implementation found at:
    //http://www.redblobgames.com/pathfinding/a-star/implementation.html#python-astar
    //***********************************
    private void findPath(Graph.Node start, Graph.Node goal)
    {
        String lastWord = null;
        PriorityQueue<Graph.Node> frontier = new PriorityQueue<Graph.Node>(1, new Comparator<Graph.Node>()
        {
            @Override
            public int compare(Graph.Node node1, Graph.Node node2)
            {
                return node1.getPriority() - node2.getPriority();
            }
        });
        frontier.add(start);
        LinkedHashMap<String, String> cameFrom = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> costSoFar = new LinkedHashMap<>();
        cameFrom.put(start.name, null);
        costSoFar.put(start.name, 0);

        while (frontier.size() > 0)
        {
            Graph.Node current = frontier.poll();

            if (current == goal)
            {
                lastWord = current.name;
                break;
            }

            for (String next : GRAPH.getNode(current.name).edges)
            {
                int new_cost = costSoFar.get(current.name) + 1; //TODO
                if (!costSoFar.containsKey(next) || new_cost < costSoFar.get(next))
                {
                    costSoFar.put(next, new_cost);
                    int priority = new_cost + levenshtein(next, goal.name);
                    Graph.Node node = GRAPH.getNode(next);
                    node.setPriority(priority);
                    frontier.add(node);
                    cameFrom.put(next, current.name);
                }
            }
        }

        if (lastWord == null)
        {
            System.out.println("NO POSSIBLE PATH: " + start.name + " " + goal.name);
            return;
        }
        printPath(cameFrom, goal);
    }
    //***********************************
    //@param LinkedHashMap<String, String> "from"
    // A hashmap holding visited nodes from creating
    // the shortest path.
    //@param Node "goal"
    // The node corresponding to the goal word.
    // Starts at the goal node and works its way back
    // through the nodes until reaching the start which
    // is initialized to null, recording each in a
    // linked list to construct the path.
    // Then prints out the path.
    //***********************************
    private void printPath(LinkedHashMap<String, String> from, Graph.Node goal)
    {
        LinkedList<String> path = new LinkedList<>();
        String current = goal.name;
        while (current != null)
        {
            path.addFirst(current);
            current = from.get(current);
        }
        for (String s : path) System.out.print(s + " ");
        System.out.println();
    }
}
