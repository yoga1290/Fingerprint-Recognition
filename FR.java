import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import javax.imageio.ImageIO;


class node
{
//    public ArrayList<Integer> neighbooringCellIndices=new ArrayList<Integer>();
    public int X,Y,Theta,Type;
    public node(int X,int Y,int Type)
    {
        this.X=X;
        this.Y=Y;
        this.Type=Type;
    }
}
public class FR
{

    /**
     * Greyscale & Binarizing 
     * @param img Image to be binarized based on the greyscaling at
     * @see http://manthapavankumar.wordpress.com/2012/12/16/converting-a-color-image-to-a-grayscale-image-programatically-using-java/
    */
    public static int[][] binarizing(BufferedImage img) throws Exception
    {
        int i,j,avg=0;
        int img_bin[][]=new int[img.getWidth()][img.getHeight()];
        BufferedImage greyscale=new BufferedImage(img.getWidth(),img.getHeight(),img.getType());
        Color c;
        for(i=0;i<img.getWidth();i++)
            for(j=0;j<img.getHeight();j++)
            {
                c=new Color(img.getRGB(i, j));
                
                avg=c.getRed()+c.getGreen()+c.getBlue();
                avg/=3;
                if(avg>120)
                    img_bin[i][j]=1;
                else
                    img_bin[i][j]=0;
                
                greyscale.setRGB(i, j, new Color(avg,avg,avg).getRGB() );
            }
        ImageIO.write(greyscale, "png", new File("greyscale.png"));
        return img_bin;
    }
    public static int[][] getFastParallelAlgoSkeleton(int img_bin[][]) throws Exception
    {
        int i,j,k,A,B;
        //clockwise
        int di[]=new int[]{0,0, -1,-1,0,1,1,1,0,-1};//1st 2 elements are useless
        int dj[]=new int[]{0,0, 0,1,1,1,0,-1,-1,-1};
        boolean EVEN=true;
        
        BufferedImage debugImg=new BufferedImage(img_bin.length, img_bin[0].length, BufferedImage.TYPE_INT_ARGB);
        
        int skeleton[][]=new int[img_bin.length][img_bin[0].length];
        for(i=1;i<img_bin.length-1;i++)
            for(j=1;j<img_bin[i].length-1;j++)
            {
                skeleton[i][j]=0;
                A=img_bin[i][j];
                B=0;
                for(k=2;k<=9;k++)// from P2+P3+...+P9
                    B+=img_bin[i+di[k]][j+dj[k]];
                
                // A=1  AND 3<= B <=6?
                if(A==1 && 3<=B && B<=6)
                {
                    if(     !EVEN
                            &&
                            img_bin[i+di[2]][j+dj[2]]*
                            img_bin[i+di[4]][j+dj[4]]*
                            img_bin[i+di[6]][j+dj[6]] == 0
                            &&
                            img_bin[i+di[4]][j+dj[4]]*
                            img_bin[i+di[6]][j+dj[6]]*
                            img_bin[i+di[8]][j+dj[8]] == 0)
                        
                                skeleton[i][j]=1;
                    
                    
                    if(     EVEN
                            &&
                            img_bin[i+di[2]][j+dj[2]]*
                            img_bin[i+di[4]][j+dj[4]]*
                            img_bin[i+di[8]][j+dj[8]] == 0
                            &&
                            img_bin[i+di[2]][j+dj[2]]*
                            img_bin[i+di[6]][j+dj[6]]*
                            img_bin[i+di[8]][j+dj[8]] == 0)
                        
                                skeleton[i][j]=1;
                    
                        
                }
                
                if(skeleton[i][j]==0)
                        debugImg.setRGB(i, j, new Color(255, 255, 255).getRGB());
                else
                        debugImg.setRGB(i, j, new Color(0, 0, 0).getRGB());
                
                EVEN=!EVEN;
            }
        
        ImageIO.write(debugImg, "png", new File("thin.png"));
        return skeleton;
    }
    
    public static int[][] getCN(int skeleton[][])//after thinning
    {
        int di[]=new int[]{0,-1,-1,-1,0,1,1,1};
        int dj[]=new int[]{1,1,0,-1,-1,-1,0,1};
        
        int i,j,k;
        
        
        int trimL=skeleton[0].length-1;
        int trimT=skeleton.length-1;
        for(i=0;i<skeleton.length;i++)
            for(j=0;j<skeleton[0].length;j++)
                if(skeleton[i][j]>0)
                {
                    trimT=Math.min(trimT, i);
                    trimL=Math.min(trimL, j);
                }
        
        int CN[][]=new int[skeleton.length-trimT][skeleton[0].length-trimL];
        
        for(i=trimT;i<skeleton.length-1;i++)
            for(j=trimL;j<skeleton[0].length-1;j++)
            {
                CN[i-trimT][j-trimL]=0;
                for(k=1;k<8;k++)
                    CN[i-trimT][j-trimL]+=Math.abs(skeleton[i+di[k-1]][j+dj[k-1]]-skeleton[i+di[k]][j+dj[k]]);
            }
        return CN;
    }
    
    public static node[] genGraph(int CN[][])
    {
        ArrayList<node> graph=new ArrayList<node>();
        int i,j,k,currentIndex=-1;
        for(i=0;i<CN.length;i++)
            for(j=0;j<CN[0].length;j++)
                if(CN[i][j]>0)
                {
//                    currentIndex= graph.size()-1;
//                    if(graph.size()==0)
//                        currentIndex=0;
                    
                    graph.add(new node(i, j, CN[i][j]));
                    //TODO: Heap out of memory fix
//                    for(k=0;k<CN.length || k<CN[i].length;k++) // K is the distance between center point & neighbooring cell(s)
//                    {
//                        if(i+k<CN.length
//                                && CN[i+k][j]>0)
//                        {
//                            graph.get(currentIndex).neighbooringCellIndices.add(graph.size());
//                            graph.add(new node(i+k, j, CN[i+k][j]));
//                        }
//                        if(j+k<CN[0].length
//                                && CN[i][j+k]>0)
//                        {
//                            graph.get(currentIndex).neighbooringCellIndices.add(graph.size());
//                            graph.add(new node(i, j+k, CN[i][j+k]));
//                        }
//                        
//                    }
                    
                    
                }
        
        
        node    GraphNodes[]=new node[graph.size()];
        graph.toArray(GraphNodes);
        return GraphNodes;
    }
    
    public static int compare(node[] graph1,node[] graph2)
    {
        
        int MinimumDistance=Integer.MAX_VALUE,missing=0;
        
        Queue<Integer> Q=new LinkedList<Integer>();
        int i,j,currentBest,currentCost,currentNode1=0,currentNode2=0,TotalDifference=0;
        
        boolean used[]=new boolean[graph2.length];
        Arrays.fill(used, false);
        
        
        currentNode1=0;
        TotalDifference=0;
        
        Q.add(currentNode1);
        Q.add(TotalDifference);
        
        
        while(!Q.isEmpty())
        {
            currentNode1=Q.remove();
            TotalDifference=Q.remove();
            
            currentCost=1<<30;
            currentBest=-1;
            
            if(     currentNode1==graph1.length-1)
            {
                MinimumDistance=Math.min(MinimumDistance,TotalDifference);
                break;
            }
            
            
            for(i=0;i<graph2.length;i++)
                if(graph1[currentNode1].Type== graph2[i].Type &&
                        
                        used[i]==false &&
                        
                    (Math.abs(graph1[currentNode1].X-graph2[i].X)+
                     Math.abs(graph1[currentNode1].Y-graph2[i].Y))<=currentCost
                  )
                {
                    
                    
                  //TODO: branching should be here, but returns Heap out of memory,so, is picking the closest 1 w same type fine?!
                    
                  currentCost=(Math.abs(graph1[currentNode1].X-graph2[i].X)+
                                Math.abs(graph1[currentNode1].Y-graph2[i].Y));
                  
                  currentBest=i;  
                  
                  
                }
            
            
            if(currentBest>-1)
            {
                used[currentBest]=true;
                
                Q.add(currentNode1+1);
                Q.add(TotalDifference+currentCost);
            }
            else
            {
                missing++;
                Q.add(currentNode1+1);
                Q.add(TotalDifference);
            }
            
        }
        System.out.println("Skipped="+missing);
        return MinimumDistance;
    }
    
    
    public static void main(String[] args) throws Exception
    {
        int i;
        node[] graph1=
             genGraph( 
                    getCN(
                        getFastParallelAlgoSkeleton(
                            binarizing(
                                ImageIO.read(new File("samples/1.png"))))));
       
        
        for(i=1;i<=5;i++)
            System.out.println("Comparing Sample#"+i+":"+
                compare(graph1, 
                    genGraph( 
                        getCN(
                            getFastParallelAlgoSkeleton(
                                binarizing(
                                    ImageIO.read(new File("samples/"+i+".png")))))) )
        );

    }