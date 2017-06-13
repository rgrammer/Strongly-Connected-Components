package stronglyConnectedComponents;

import java.util.*;
 
public class STRONGLY_CONNECTED_COMPONENTS
{
    private int first = 0;
    private int[] first_vertex;
    private int vertices_explored[];
    private int finishing_time_of_vertex[];
    private int finishing_time = 1;
    private int number_of_vertices;
    private Stack<Integer> stack;
    private Map<Integer, Integer> finishing_time_map;
 
    public STRONGLY_CONNECTED_COMPONENTS(int number_of_vertices)
    {
        this.number_of_vertices = number_of_vertices;
        first_vertex = new int[number_of_vertices + 1];
        vertices_explored = new int[number_of_vertices + 1]; //Values in 'vertices_explored' correspond to colors of vertices
        finishing_time_of_vertex = new int[number_of_vertices + 1];
        stack = new Stack<Integer>();
        finishing_time_map = new HashMap<Integer, Integer>(); //Map holds time for repective vertex
    }
    
    //Function to check if value is in array
    public static boolean inArray(int[] array, int object) {
    	Arrays.sort(array);
    	int index = Arrays.binarySearch(array, object);
    	if (index >= 0) {
    		return true;
    	}
    	else
    		return false;
    }

 
    //Strongly Connected Component Algorithm which takes in user entered adjacency matrix
    public void strongConnectedComponent(int ADJACENCY_MATRIX[][])
    {
    	//DFS(G) which computes finishing times u.f for each vertex u
        for (int i = number_of_vertices; i > 0; i--)
        {
        	//If corresponding value is 0 representing White, run DFS_VISIT_u
            if (vertices_explored[i] == 0)
            {
                DFS_VISIT_u(ADJACENCY_MATRIX, i);
            }
        }
        
        //Compute G^T
        int TRANSPOSE_MATRIX[][] = new int[number_of_vertices + 1][number_of_vertices + 1];
        for (int i = 1; i <= number_of_vertices; i++)
        {
            for (int j = 1; j <= number_of_vertices; j++)
            {
                if (ADJACENCY_MATRIX[i][j] == 1)
                    TRANSPOSE_MATRIX[finishing_time_of_vertex[j]][finishing_time_of_vertex[i]] = ADJACENCY_MATRIX[i][j];
            }
        }
 
        //Reset vertices_explored to 0 so the array can be used for determining color of v
        for (int i = 1; i <= number_of_vertices; i++)
        {
            vertices_explored[i] = 0;
            first_vertex[i] = 0;
        }
        
        //DFS(G^T) is called to compute the finishing times in order of decreasing u.f as computed in first DFS call
        for (int i = number_of_vertices; i > 0; i--)
        {
        	//If v.color is 0 i.e. White, then Call DFS_Visit_v
            if (vertices_explored[i] == 0)
            {
                first = i;
                DFS_VISIT_v(TRANSPOSE_MATRIX, i);
            }
        }
    }
 
    //Algorithm for DFS-VISIT(G, u) which computes finishing times for all vertices
    public void DFS_VISIT_u(int ADJACENCY_MATRIX[][], int source)
    {
    	//White vertex has just been discovered and is colored Grey
        vertices_explored[source] = 1;
        //stack is used to keep order of discovered vertices 
        stack.push(source);
        int i = 1;
        int element = source;
 
        
        while (!stack.isEmpty())
        {
            element = stack.peek();
            i = 1;
            while (i <= number_of_vertices)
            {
            	//All vertices are explored
                if (ADJACENCY_MATRIX[element][i] == 1 && vertices_explored[i] == 0)
                {
                    stack.push(i);
                    vertices_explored[i] = 1;
                    element = i;
                    i = 1;
                    continue;
                }
                i++;
            }
            
            //time = time + 1, u.d = time
            int poped = stack.pop();
            int time = finishing_time++;
            finishing_time_of_vertex[poped] = time;
            //map holds time for each vertex
            finishing_time_map.put(time, poped);
        }
    }
 
    //Algorithm for DFS-Visit(G, v)
    public void DFS_VISIT_v(int TRANSPOSE_MATRIX[][], int source)
    {
        vertices_explored[source] = 1;
        first_vertex[finishing_time_map.get(source)] = first;
        //stack is used to keep order of all discovered vertices
        stack.push(source);
        int i = 1;
        int element = source;
        while (!stack.isEmpty())
        {
            element = stack.peek();
            i = 1;
            while (i <= number_of_vertices)
            {
            	//Vertices are discovered in order of decreasing u.f
                if (TRANSPOSE_MATRIX[element][i] == 1 && vertices_explored[i] == 0)
                {
                    if (first_vertex[finishing_time_map.get(i)] == 0)
                        first_vertex[finishing_time_map.get(i)] = first;
                    stack.push(i);
                    vertices_explored[i] = 1; //u.color = black
                    element = i;
                    i = 1;
                    continue;
                }
                i++;
            }
            stack.pop();
        }
    }
 
    public static void main(String... arg)
    { 
        int number_of_vertices;
        Scanner scanner = null;
        try
        {
        	//User is asked to input number of nodes in the graph 
            System.out.println("How many vertices does your graph have?");
            scanner = new Scanner(System.in);
            number_of_vertices = scanner.nextInt();
 
            //User enters the adjacency matrix which is read in
            int ADJACENCY_MATRIX[][] = new int[number_of_vertices + 1][number_of_vertices + 1];
            System.out.println("Enter the adjacency matrix for your graph, separate each entry with a space");
            for (int i = 1; i <= number_of_vertices; i++)
                for (int j = 1; j <= number_of_vertices; j++)	
                    ADJACENCY_MATRIX[i][j] = scanner.nextInt();
 
            //SCC algorithm runs
            STRONGLY_CONNECTED_COMPONENTS strong = new STRONGLY_CONNECTED_COMPONENTS(number_of_vertices);
            strong.strongConnectedComponent(ADJACENCY_MATRIX);
 
            //The vertices of each tree in the depth-first forest are output as separate strongly connected components
            System.out.println("Vertices belong to the following strongly connected components:");
            
            int componentKey[] = {0, 7, 8, 9, 10};           
         
            int r = 0;
            
            //Iterate through vertices and list the strongly connected component they are associated with
            for (int i = 1; i < strong.first_vertex.length; i++)
            {            	
            	for (r = 0; r < componentKey.length; r++)
            	{
            		if (componentKey[r] == strong.finishing_time_map.get(strong.first_vertex[i]))
            			break;
            	}            	
            System.out.println( "Vertex " + i + " belongs to the Strongly Connected Component " + r);           		
            }
        } catch (InputMismatchException inputMismatch)
        {	
            System.out.println("Incorrect Input Format: ");
        }
    }
}