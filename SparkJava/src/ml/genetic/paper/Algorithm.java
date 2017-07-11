package ml.genetic.paper;

public class Algorithm {
	 /* GA parameters */
    private static final double uniformRate = 0.7;
    private static final double mutationRate = 0.001;
    private static final int tournamentSize = 5;
    private static final boolean elitism = false;
 
    
    // Evolve a population
    public static Population evolvePopulation(Population pop) {
    	FitnessCalc.fitnessTotal=0;
        Population newPopulation = new Population(pop.size(), false);

        // Keep our best individual
        if (elitism) {
            newPopulation.saveChromosome(0, pop.getFittest());
        }

        // Crossover population
        int elitismOffset;
        if (elitism) {
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }
        // Loop over the population size and create new individuals with
        // crossover
        for (int i = elitismOffset; i < pop.size(); i++) {
        	Chromosome indiv1 = tournamentSelection(pop);
        	Chromosome indiv2 = tournamentSelection(pop);
        	Chromosome newIndiv = crossover(indiv1, indiv2);
            newPopulation.saveChromosome(i, newIndiv);
        }

        // Mutate population
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            mutate(newPopulation.getChromosome(i));
        }

        return newPopulation;
    }

    // Crossover individuals
    private static Chromosome crossover(Chromosome indiv1, Chromosome indiv2) {
    	Chromosome newSol = new Chromosome();
        // Loop through genes
        for (int i = 0; i < indiv1.size(); i++) {
            // Crossover
            if (Math.random() <= uniformRate) {
                newSol.setGene(i, indiv1.getGene(i));
            } else {
                newSol.setGene(i, indiv2.getGene(i));
            }
        }
        return newSol;
    }

    // Mutate an individual
    private static void mutate(Chromosome indiv) {
        // Loop through genes
        for (int i = 0; i < indiv.size(); i++) {
            if (Math.random() <= mutationRate) {
                // Create random gene
                byte gene = (byte) Math.round(Math.random());
                indiv.setGene(i, gene);
            }
        }
    }

    // Select individuals for crossover
    private static Chromosome tournamentSelection(Population pop) {
        // Create a tournament population
        Population tournament = new Population(tournamentSize, false);
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveChromosome(i, pop.getChromosome(randomId));
        }
        // Get the fittest
        Chromosome fittest = tournament.getFittest();
        return fittest;
    }
}
