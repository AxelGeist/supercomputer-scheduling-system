package commons;

public class Resource {
    private final int cpu;
    private final int gpu;
    private final int mem;

    /**
     * Constructor for Resource.
     *
     * @param cpu number of cpu resource
     * @param gpu number of gpu resource
     * @param mem number of memory resource
     */
    public Resource(int cpu, int gpu, int mem) {
        this.cpu = cpu;
        this.gpu = gpu;
        this.mem = mem;
    }

    public int getCpu() {
        return this.cpu;
    }

    public int getGpu() {
        return this.gpu;
    }

    public int getMem() {
        return this.mem;
    }

}

