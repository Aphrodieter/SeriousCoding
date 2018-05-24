"""
@Author: Joris van Vugt, Moira Berens

Implementation of the variable elimination algorithm for AISPAML assignment 3

"""
import copy
class VariableElimination():

    def __init__(self, network):
        self.network = network
        self.addition_steps =  0
        self.multiplication_steps = 0
        
        
    def getChildren(self,node):
        children = []
        for n in self.network.parents:
            if (node in self.network.parents[n]):
                children.append(n)
        return children
                
    def hasChildren(self,node):
        for n in self.network.parents:
            if (node in self.network.parents[n]):
                return True
        return False
    
    def isBarren(self,query,observerd,node):
        if (node in query or node in observerd):
            return False
        elif (self.hasChildren(node)):
            for child in self.getChildren(node):
                if not self.isBarren(query,observerd,child):
                    return False
        return True
    
    def makeFactor(self,table, observed):
        fac = table
        names = table.columns.values
        for evi in observed:
            if evi in names:
                #delete all rows where the observerd variable holds
                fac = fac.ix[fac[evi] != str(observed[evi])]
                
        print fac
        return fac
    
    def multiplyFactors(self,Z,factors):
        toMultiply = []
        for factor in factors:
            names = factor.columns.values
            if Z in names:
                toMultiply.append(factor)
                
        newFac = copy.deepCopy(toMultiply)

        for value in self.network.values[Z]:
            probsToMultiply = []
            for fac in toMultiply:
                probsToMultiply.append((fac.ix[fac[Z] == value])[fac.columns[-1]])
            
        
        
        return probsToMultiply
                

    def run(self, query, observed, elim_order):
        """
        Use the variable elimination algorithm to find out the probability
        distribution of the query variable given the observed variables

        Input:
            query:      The query variable
            observed:   A dictionary of the observed variables {variable: value}
            elim_order: Either a list specifying the elimination ordering
                        or a function that will determine an elimination ordering
                        given the network during the run

        Output: A variable holding the probability distribution
                for the query variable

        """
        probs = []
        
        for node in self.network.probabilities:
            #append probability dataframes to probs list 
            if not self.isBarren(query,observed, node):
                probs.append(self.network.probabilities[node])
                        
        factors = []
        for df in probs:
            #make new factor for every prob table
            factors.append(self.makeFactor(df,observed))
            
        
        for Z in elim_order:
            print self.multiplyFactors(Z,factors)
            
        #factors
        #print 'test', self.makeFactor(probs[1],{'Earthquake'})
        #print 'factors: ', factors
        #print self.hasChildren(elim_order[2])
        #print self.getChildren('Alarm')

