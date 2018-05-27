"""
@Author: Joris van Vugt, Moira Berens

Implementation of the variable elimination algorithm for AISPAML assignment 3

"""
import copy
import pandas as pd
import numpy as np
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
        return fac
    
    def multiplyFactors(self,Z,toMultiply):
        length = len(toMultiply)
        if length == 0:
            return None
        if length == 1:
            return toMultiply[0]
        else:
            common_values = np.intersect1d(toMultiply[0].columns.values, toMultiply[1].columns.values)
            newFac = pd.merge(toMultiply[0],toMultiply[1],on=tuple(common_values[:-1]))
            for i in range(2,length):
                common_values = np.intersect1d(newFac.columns.values, toMultiply[i].columns.values)
                #Now the only possible common values are values which don't start with prob. thats why only common_values
                newFac = pd.merge(newFac,toMultiply[i],on=tuple(common_values))

        #get all column entries where the name of the column starts with 'prob'
        probCols = [newFac[col] for col in newFac.columns.values if col.startswith('prob')]
        #delete all columns from newFac where the column name contains 'prob'
        newFac = newFac[newFac.columns.drop(list(newFac.filter(regex='prob')))]
        
        #Multiply prob columns with each other
        newProbs = probCols[0]
        for i in range(1,len(probCols)):
            newProbs = newProbs*probCols[i]
        
        #assign new prob entry with updated probs
        newFac['prob'] = newProbs
        
        return newFac
    
    def sumout(self,Z,fac):
        if fac is not None:
            toSum = []
            for value in self.network.values[Z]:
                part = fac.ix[fac[Z] == value]
                if not part.empty:
                    toSum.append(part)
            #print toSum,'Z',Z
            
            for part in toSum:
                del part[Z]
            
            if len(toSum) == 1:
                return toSum[0]
                
            else:
                cols = toSum[0].columns.values
                #tuple: because nparray isn't hashable
                if len(cols) > 1:
                    on = tuple(cols[:-1])
                else:
                    return None
                newFac = pd.merge(toSum[0],toSum[1],on=on)
                for i in range(2,len(toSum)):
                    newFac = pd.merge(newFac,toSum[i],on=on)
                  
                #get all column entries where the name of the column starts with 'prob'
                probCols = [newFac[col] for col in newFac.columns.values if col.startswith('prob')]
        
                #delete all columns from newFac where the column name contains 'prob'
                newFac = newFac[newFac.columns.drop(list(newFac.filter(regex='prob')),inplace = True)]
        
                #add prob columns on each other
                newProbs = probCols[0]
                for i in range(1,len(probCols)):
                    newProbs = newProbs+probCols[i]
        
                #assign new prob entry with updated probs
                newFac['prob'] = newProbs
            return newFac

    def eliminate(self,factors,query,elim_order):
        for Z in elim_order:
            if Z == query:
                continue
            toMultiply = []
            toPop = []
            for ind,factor in enumerate(factors):
                names = factor.columns.values
                if Z in names:
                    toMultiply.append(factor)
                    toPop.append(ind)
            
            for i in toPop:
                factors.pop(i)
                toPop[:] = [x - 1 for x in toPop]
                
            newFac = self.sumout(Z,self.multiplyFactors(Z,toMultiply))
            if newFac is not None:
                factors.append(newFac)
        
        if len(factors) != 1:
            raise ValueError('The length of the factor array is still not one after applying the elimination algorithm')
        
        return factors[0]
    
    def normalize(self,factors):
        factors[0] = factors[0].round(4) 
        
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
        if query not in self.network.nodes:
            raise ValueError('No node with name',query)
        probs = []
        
        for node in self.network.probabilities:
            #append probability dataframes to probs list 
            if not self.isBarren(query,observed, node):
                probs.append(self.network.probabilities[node])
                        
        factors = []
        for df in probs:
            #make new factor for every prob table
            factors.append(self.makeFactor(df,observed))
            
        
        self.eliminate(factors,query,elim_order)
        self.normalize(factors)
        

        print factors[0]
            
        #factors
        #print 'test', self.makeFactor(probs[1],{'Earthquake'})
        #print 'factors: ', factors
        #print self.hasChildren(elim_order[2])
        #print self.getChildren('Alarm')

