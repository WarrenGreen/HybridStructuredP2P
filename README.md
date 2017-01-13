#Hybrid-Structured P2P Simulation

I have been reading Tennebaum's Distributed Systems book the past few weeks and have been loving it.
In Chapter 2: Architectures, he covers a P2P design called Hybrid-Structured. While the concept isn't too
complicated, the visualization really stuck out to me.

![cool](/imgs/img1.png?raw=true "")

The concept is a distributed system of nodes, all of which have two threads. The lower-thread (shown in image below)
will randomly connect to other nodes in the network. During the connection, this thread will collect some
weight or cost value. For example, latency. The lower thread then passes this information up to the upper-thread.
The upper-thread will then rank and keep n connections, specifically the n higher ranked connections. Eventually
through time we see a well structured network form from the initial randomness. We see this demonstrated well in
the image above.

####My Progress

I was able to quickly make a simulation of this process by spawning n\*m nodes which are threads that spawn two
more threads. n\*m nodes as to represent a grid which would be a basic visualization test. The hope was to make a GUI
editor so I could draw node clusters that would become clean grids. Where did I end? With a cool simulation and a
*strong memory of why I never try to do Java graphics.*

![hybrid-structured](/imgs/img2.png?raw=true "")
