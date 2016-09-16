/*
 * (C) Copyright 2008-2016, by Ilya Razenshteyn and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */
package org.jgrapht.generate;

import java.util.*;

import org.jgrapht.*;

/**
 * Generates directed or undirected
 * <a href = "http://mathworld.wolfram.com/Scale-FreeNetwork.html">scale-free network</a> of any
 * size. Scale-free network is a connected graph, where degrees of vertices are distributed in
 * unusual way. There are many vertices with small degrees and only small amount of vertices with
 * big degrees.
 *
 * @author Ilya Razenshteyn
 */
public class ScaleFreeGraphGenerator<V, E>
    implements GraphGenerator<V, E, V>
{
    private int size; // size of graphs, generated by this instance of generator
    private long seed; // initial seed
    private Random random; // the source of randomness

    /**
     * Constructs a new <tt>ScaleFreeGraphGenerator</tt>.
     *
     * @param size number of vertices to be generated
     */
    public ScaleFreeGraphGenerator(int size)
    {
        if (size < 0) {
            throw new IllegalArgumentException("invalid size: " + size + " (must be non-negative)");
        }
        this.size = size;
        random = new Random();
        seed = random.nextLong();
    }

    /**
     * Constructs a new <tt>ScaleFreeGraphGenerator</tt> using fixed <tt>
     * seed</tt> for the random generator.
     *
     * @param size number of vertices to be generated
     * @param seed initial seed for the random generator
     */
    public ScaleFreeGraphGenerator(int size, long seed)
    {
        if (size < 0) {
            throw new IllegalArgumentException("invalid size: " + size + " (must be non-negative)");
        }
        this.size = size;
        random = new Random();
        this.seed = seed;
    }

    /**
     * Generates scale-free network with <tt>size</tt> passed to the constructor. Each call of this
     * method produces identical output (but if <tt>target</tt> is an undirected graph, the
     * directions of edges will be lost).
     *
     * @param target receives the generated edges and vertices; if this is non-empty on entry, the
     *        result will be a disconnected graph since generated elements will not be connected to
     *        existing elements
     * @param vertexFactory called to produce new vertices
     * @param resultMap unused parameter
     */
    @Override
    public void generateGraph(
        Graph<V, E> target, VertexFactory<V> vertexFactory, Map<String, V> resultMap)
    {
        random.setSeed(seed);
        List<V> vertexList = new ArrayList<>();
        List<Integer> degrees = new ArrayList<>();
        int degreeSum = 0;
        for (int i = 0; i < size; i++) {
            V newVertex = vertexFactory.createVertex();
            target.addVertex(newVertex);
            int newDegree = 0;
            while ((newDegree == 0) && (i != 0)) // we want our graph to be
                                                 // connected

            {
                for (int j = 0; j < vertexList.size(); j++) {
                    if ((degreeSum == 0) || (random.nextInt(degreeSum) < degrees.get(j))) {
                        degrees.set(j, degrees.get(j) + 1);
                        newDegree++;
                        degreeSum += 2;
                        if (random.nextInt(2) == 0) {
                            target.addEdge(vertexList.get(j), newVertex);
                        } else {
                            target.addEdge(newVertex, vertexList.get(j));
                        }
                    }
                }
            }
            vertexList.add(newVertex);
            degrees.add(newDegree);
        }
    }
}

// End ScaleFreeGraphGenerator.java
