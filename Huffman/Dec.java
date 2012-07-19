

/// Umut Can Genlik CS 338 Lab1


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Dec {

    private static class Node {
        public double f;
        public char c;
        public Node l = null;
        public Node r = null;
        
        public Node(Node n1, Node n2) {
            l = n1;
            r = n2;
            f = n1.f + n2.f;
        }
        
        public Node(String fs) {
            String[] g = fs.split(",");
            c = g[0].charAt(0);
            f = Double.valueOf(g[1]).doubleValue();
        }
        
        public boolean isLeaf() {
            return l==null;
        }
    }
    
    private static Node removeSmallest(Node[] nodes, int nsize) {
        int lfi = -1;
        double lf = Double.MAX_VALUE;
        
        for (int i = 0; i < nsize; i++) {
            if (nodes[i].f < lf) {
                lf = nodes[i].f;
                lfi = i;
            }
        }
        
        Node r = nodes[lfi];
        nodes[lfi] = nodes[nsize-1]; //Override this one with the last node so that I can remove the last node.
        
        return r;
    }

    public static Node create() {
        String tbl =
                "A,8.4966%"
                + "B,2.0720%"
                + "C,4.5388%"
                + "D,3.3844%"
                + "E,11.1607%"
                + "F,1.8121%"
                + "G,2.4705%"
                + "H,3.0034%"
                + "I,7.5448%"
                + "J,0.1965%"
                + "K,1.1016%"
                + "L,5.4893%"
                + "M,3.0129%"
                + "N,6.6544%"
                + "O,7.1635%"
                + "P,3.1671%"
                + "Q,0.1962%"
                + "R,7.5809%"
                + "S,5.7351%"
                + "T,6.9509%"
                + "U,3.6308%"
                + "V,1.0074%"
                + "W,1.2899%"
                + "X,0.2902%"
                + "Y,1.7779%"
                + "Z,0.2722%"
                + " ,0.0005%"
                + ".,0.0004";

        Node[] nodes = new Node[28];
        String[] comp = tbl.split("%");
        for (int i = 0; i < 28; i++) {
            nodes[i] = new Node(comp[i]);
        }
        
        //Create tree from list
        int nsize = 28;
        while (nsize>1) {
            Node n1 = removeSmallest(nodes, nsize);
            nsize--; // Removing the last node
            Node n2 = removeSmallest(nodes, nsize);
            nsize--; // Removing the last node
            
            nodes[nsize] = new Node(n1, n2);
            nsize++; // Adding new parent node
        }
        
        //Return root of the tree
        return nodes[0];
    }


    public static Node doChar(Node current, int c, Node root, FileOutputStream fostream) throws IOException {
        if (current.isLeaf()) {
            fostream.write(current.c);
            //System.out.print(current.c);
            current = root;
        }
        return (c&0x80)==0 ? current.l : current.r;
    }
    
    public static void main(String[] args) {
        Node root = create();

        try {
            FileInputStream fstream = new FileInputStream(args[0]);
            FileOutputStream fostream = new FileOutputStream(args[0].replace(".enc", ".dec"));

            int c = fstream.read();
            Node current = root;
            while (c != -1) {
                for (int i = 0; i < 8; i++) {
                    current= doChar(current, c, root, fostream);
                    c <<= 1;
                }
                c = fstream.read();
            }
            
            doChar(current, c, root, fostream);

            fstream.close();
            fostream.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
