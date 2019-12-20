import agh.po.Properties.Genes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class GenesTest
{
    Genes genes1 = new Genes();
    Genes genes2 = new Genes();
    Genes genes3 = new Genes();
    Genes genes4 = new Genes();

    Genes babyGenes1= new Genes(genes1, genes2);
    Genes babyGenes2= new Genes(genes3, genes4);
    Genes babyGenes3= new Genes(genes1, genes3);

    @Test
    void testGenes()
    {
        int [] gensOccurrences1=new int [8];
        int [] gensOccurrences2=new int [8];
        int [] gensOccurrences3=new int [8];
        int [] gensOccurrences4=new int [8];
        int [] babyGensOccurrences1=new int [8];
        int [] babyGensOccurrences2=new int [8];
        int [] babyGensOccurrences3=new int [8];

        for(int i=0; i<32; i++)
        {
            assertTrue(genes1.getGenAtIndex(i)>=0 && genes1.getGenAtIndex(i)<8 );
            assertTrue(genes2.getGenAtIndex(i)>=0 && genes2.getGenAtIndex(i)<8 );
            assertTrue(genes3.getGenAtIndex(i)>=0 && genes3.getGenAtIndex(i)<8 );
            assertTrue(genes4.getGenAtIndex(i)>=0 && genes4.getGenAtIndex(i)<8 );
            assertTrue(babyGenes1.getGenAtIndex(i)>=0 && babyGenes1.getGenAtIndex(i)<8 );
            assertTrue(babyGenes2.getGenAtIndex(i)>=0 && babyGenes2.getGenAtIndex(i)<8 );
            assertTrue(babyGenes3.getGenAtIndex(i)>=0 && babyGenes3.getGenAtIndex(i)<8 );
            gensOccurrences1[genes1.getGenAtIndex(i)]++;
            gensOccurrences2[genes2.getGenAtIndex(i)]++;
            gensOccurrences3[genes3.getGenAtIndex(i)]++;
            gensOccurrences4[genes4.getGenAtIndex(i)]++;
            babyGensOccurrences1[babyGenes1.getGenAtIndex(i)]++;
            babyGensOccurrences2[babyGenes2.getGenAtIndex(i)]++;
            babyGensOccurrences3[babyGenes3.getGenAtIndex(i)]++;
        }

        for(int i=0; i<8; i++)
        {
            assertTrue(gensOccurrences1[i]>0);
            assertTrue(gensOccurrences2[i]>0);
            assertTrue(gensOccurrences3[i]>0);
            assertTrue(gensOccurrences4[i]>0);
            assertTrue(babyGensOccurrences1[i]>0);
            assertTrue(babyGensOccurrences2[i]>0);
            assertTrue(babyGensOccurrences3[i]>0);
        }

    }
}
