package agh.po.Properties;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Genes {

    private static final int genesQuantity=32;
    private static final int typeOfGenesQuantity=8;
    private int [] genotype=new int [genesQuantity];

    public Genes()
    {
        randomlyGenerateGenotype();
    }

    public Genes(Genes parent1Genotype, Genes parent2Genotype)
    {
       generateBabyGenotype(parent1Genotype, parent2Genotype);
    }

    private void randomlyGenerateGenotype() {

        int [] genesOccurrences = new int [typeOfGenesQuantity];
        for (int i = 0; i < typeOfGenesQuantity; i++) {
            genesOccurrences[i] = 0;
        }
        for (int i = 0; i < genesQuantity; i++) {
            this.genotype[i] = ThreadLocalRandom.current().nextInt(typeOfGenesQuantity);
            genesOccurrences[this.genotype[i]]+=1;
        }
         correctGenotype(this.genotype, genesOccurrences);

    }

    private void correctGenotype(int [] genotype, int [] genesOccurrences)
    {
        for(int i=0; i<typeOfGenesQuantity; i++)
        {
            if(genesOccurrences[i]==0)
            {
                int randomIndex=ThreadLocalRandom.current().nextInt(genesQuantity);
                while(genesOccurrences[genotype[randomIndex]]<=1)
                {
                    randomIndex+=1;
                    if(randomIndex>genotype.length)
                    {
                        randomIndex=randomIndex%genotype.length;
                    }
                }
                genesOccurrences[genotype[randomIndex]]-=1;
                genotype[randomIndex]=i;
                genesOccurrences[i]+=1;
            }
        }
        Arrays.sort(genotype);
    }

    private void generateBabyGenotype(Genes parent1Genotype, Genes parent2Genotype){
        int [] genesOccurrences = new int [8];
        int div1 = ThreadLocalRandom.current().nextInt(1, genesQuantity-2);
        int div2 = ThreadLocalRandom.current().nextInt(div1+1, genesQuantity-1);
        for (int i=0; i<typeOfGenesQuantity; i++)
            genesOccurrences[i]=0;
        for (int i=0; i<32; i++) {
            if (i <= div1)
                this.genotype[i] = parent1Genotype.genotype[i];
            else if (i <= div2)
                this.genotype[i] = parent2Genotype.genotype[i];
            else
                this.genotype[i] = parent1Genotype.genotype[i];
            genesOccurrences[this.genotype[i]]+=1;
        }
        correctGenotype(this.genotype,genesOccurrences);
    }

    public int getGenAtIndex(int index)
    {
        return this.genotype[index];
    }


}
