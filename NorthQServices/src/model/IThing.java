package model;

public interface IThing {
    @Override
    public String toString();

    public String getNodeID();

    public boolean equals();
}