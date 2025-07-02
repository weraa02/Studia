#!/usr/bin/python3
import random
import matplotlib.pyplot as plt
import numpy as np
import networkx as nx
from copy import deepcopy

def opozT(G, N, m):
    #średnie opóźnieniee pakietu w sieci
    tt = 0
    for edge in G.edges():
    	a = G.edges[edge]['a']
    	c = G.edges[edge]['c']
    	if (((c/m)-a) != 0) and (a/((c/m)-a)>0):
    		tt += a/((c/m)-a)
    	else:
    		#print("none")
    		return None
    
    tt = tt / np.sum(N)
    return tt

#niezawodnosc    
def nzwd(N, p, Tmax, G, m):
	succes = 0 #spojny
	fail = 0 #niespojny
	rell = 0 #T dobry
	
	for _ in range(1000):
		Gcopy = deepcopy(G)
		for edge in list(Gcopy.edges()):
			if random.random() > p:
				Gcopy.remove_edge(*edge) #jako dwa node
				if not nx.is_connected(Gcopy):
                    			fail += 1
                    			break
		if nx.is_connected(Gcopy):
			succes += 1
			#generujemy a
			nx.set_edge_attributes(Gcopy, 0, "a")
			nodes = nx.number_of_nodes(Gcopy)  
			for i in range(nodes):
				for j in range(nodes):
					path = nx.shortest_path(Gcopy, i, j)
					for n in range(len(path) - 1):
						Gcopy[path[n]][path[n + 1]]["a"] += N[i-1][j-1]
			#nowe T
			T = opozT(Gcopy, N, m)			
			if T is not None and T < Tmax:
				rell +=1		
	
	plt.figure(figsize=(20, 20))
	pos = nx.spring_layout(G)
	nx.draw_networkx_edge_labels(G, pos)
	nx.draw_networkx_labels(G, pos, font_color="w")
	nx.draw(G, pos)
	plt.title('Graf glowny')
	#plt.show()/**/
	plt.close()
	
	return rell/1000


def higherN(G, N, zmiana, p, Tmax, m):
	Gcopy = deepcopy(G)
	#nowe wartosci
	NN = deepcopy(N)
	for i in range(20):
		for j in range(20):
			if i == j:
				NN[i][j] = 0
			else:
				NN[i][j] = N[i][j] + zmiana 	
	#generujemy a
	nx.set_edge_attributes(Gcopy, 0, "a")
	nodes = nx.number_of_nodes(Gcopy)  
	for i in range(nodes):
		for j in range(nodes):
			path = nx.shortest_path(Gcopy, i, j)
			for n in range(len(path) - 1):
				Gcopy[path[n]][path[n + 1]]["a"] += NN[i-1][j-1]

	#niezawodnosc
	NZW = nzwd(NN, p, Tmax, Gcopy, m)
	print("N+",zmiana,"; Niezawodnosc:",NZW)

def zmianyc(N, p, Tmax, G, m, c):
    cc = c/5
    print("\nc = 0.2*c = ",cc)
    nx.set_edge_attributes(G, cc, "c")
    NZW = nzwd(N, p, Tmax, G, m)
    print("niezawodnosc:",NZW)
    
    cc = c/3
    print("c = 0.33*c = ",cc)
    nx.set_edge_attributes(G, cc, "c")
    NZW = nzwd(N, p, Tmax, G, m)
    print("niezawodnosc:",NZW)
    
    cc = c/2
    print("c = 0.5*c = ",cc)
    nx.set_edge_attributes(G, cc, "c")
    
    NZW = nzwd(N, p, Tmax, G, m)
    print("niezawodnosc:",NZW)
    
    cc = 2*c
    print("c = 2*c = ",cc)
    nx.set_edge_attributes(G, cc, "c")
    
    NZW = nzwd(N, p, Tmax, G, m)
    print("niezawodnosc:",NZW)
    
    cc = c*c
    print("c = c*c = ",cc)
    nx.set_edge_attributes(G, cc, "c")
    
    NZW = nzwd(N, p, Tmax, G, m)
    print("niezawodnosc:",NZW)

def noweE(G, N, c, p, Tmax, m):
	forC = nx.get_edge_attributes(G, "c").values()
	newC = sum(forC)/len(forC) #suma/przez ilosc
	looking = 0
	while looking<2:
		a = random.randint(0,19)
		b = random.randint(0,19)
		if not (G.has_edge(a,b) or a==b) :
			G.add_edge(a, b)
			G[a][b]["c"] = newC
			looking +=1
	# nie dodaje a, bo to sie zrobi przy nzwd
	print("krawedzie: ", G.number_of_edges(), "niezawodnosc:",nzwd(N, p, Tmax, G, m))
	return G;

def main():

    # GRAF
    G = nx.Graph()
    # wierzcholki
    for i in range(19):
        G.add_node(i)
    # krawedzie
    for i in range(9):
    	G.add_edge(i, i + 1)
    	if i % 2 == 0:
          G.add_edge(i, i + 10)
    for i in range(10,19):
        G.add_edge(i, i + 1)

    G.add_edge(9,0)
    G.add_edge(10,19)
    G.add_edge(5, 15)
    G.add_edge(11, 17)
    G.add_edge(19, 13)
    
    #jesli nie spojny to koniec
    if nx.is_connected(G)== False:
    	print("Graf nie spójny")
    	return
    
    # macierz natezen (o losowych wartosciach w przedziale 1-9)   		
    minwart = 1 
    maxwart = 9
    N = []
    print("		macierz N:")
    for i in range(20):
        N.append([])
        for j in range(20):
            if i == j:
                # nic nie bedzie jesli od sb do sb
                N[i].append(0)
            else:
                # losuj od 1 do 9
                N[i].append(random.randint(minwart, maxwart))
    print(np.matrix(N))    
    
    # przepływ a
    # set_edge_attributes(graf, values, name)
    nx.set_edge_attributes(G, 0, "a")
    
    nodes = nx.number_of_nodes(G)  
    # chodzenie po wierzchołkach
    for i in range(nodes):
        for j in range(nodes):
            # oblicza najkrósza sciezke, potem "chodzimy" po jej punktach
            path = nx.shortest_path(G, i, j)
            for n in range(len(path) - 1):
                G[path[n]][path[n + 1]]["a"] += N[i-1][j-1]

    # przepustowosc
    c = 0
    m = 1000 #przypuszczalna srednia wielkosc pakietow
    # oblciza sume wszystkich natężeń
    for i in range(19):
        for j in range(19):
            c += N[i][j]
            
    c = m * c
    #c = 200000
    # ustawia wartosc c bokow
    nx.set_edge_attributes(G, c, "c")
    
    #sprawdzenie c>a  
    for edge in G.edges():
    	if G.edges[edge]['c'] < G.edges[edge]['a']:
    		print("Przepływ większy niż przepustowosc")
    		return None

    #rysowanie wykresu
    plt.figure(figsize=(20, 20))
    pos = nx.spring_layout(G)
    nx.draw_networkx_edge_labels(G, pos)
    nx.draw_networkx_labels(G, pos, font_color="w")
    nx.draw(G, pos)
    plt.title('Graf glowny')
    plt.show()
    plt.close()
    
    #sredni czas opoznienia
    T=opozT(G, N, m)
    
    p=0.80
    Tmax= 0.1
    #niezawodnosc
    NZW = nzwd(N, p, Tmax, G, m)
    
    print("p:",p," Tmax: ",Tmax," m:",m)
    print("c:",c)
    print("niezawodnosc:",NZW)
    
        #ZMIANY N 
    #higherN(G, N, 5, p, Tmax, m)
    #higherN(G, N, 10, p, Tmax, m)
    #higherN(G, N, 15, p, Tmax, m)
    #higherN(G, N, 20, p, Tmax, m)
    #higherN(G, N, 25, p, Tmax, m)
    
    #zmianyc(N, p, Tmax, G, m, c)
    
    GG = deepcopy(G)
    GG = noweE(GG,N,c, p, Tmax, m)
    #GG = noweE(GG,N,c, p, Tmax, m)
    #GG = noweE(GG,N,c, p, Tmax, m)
    #GG = noweE(GG,N,c, p, Tmax, m)
    #GG = noweE(GG,N,c, p, Tmax, m)
    #GG = noweE(GG,N,c, p, Tmax, m)
    #GG = noweE(GG,N,c, p, Tmax, m)
    #GG = noweE(GG,N,c, p, Tmax, m)
    
    plt.figure(figsize=(20, 20))
    pos = nx.spring_layout(GG)
    nx.draw_networkx_edge_labels(GG, pos)
    nx.draw_networkx_labels(GG, pos, font_color="w")
    nx.draw(GG, pos)
    plt.title('Graf po dodaniu')
    #plt.show()
    plt.close()


if __name__ == "__main__":
    main()

