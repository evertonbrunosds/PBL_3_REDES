package model;

public class RecycleBin {

    private int usage;
    private final Long id;
    private final int latitude;
    private final int longitude;
    public final int limit;

    public RecycleBin(final long id, final int latitude, final int longitude, final int limit) {
        usage = 0;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.limit = limit;
    }

    public void addUsage(final int usage) {
        final Integer percent = (int) (((this.usage * 1.0) / (limit * 1.0)) * 100);
        System.out.println("LIXEIRA A [" + id.toString() + "] ESTÁ COM " + percent.toString() + "% DE SUA CAPACIDADE TOTAL.");
        if (this.usage + usage <= limit) {
            this.usage += usage;
        } else {
            this.usage = limit;
        }
    }
    
    public boolean isCritical() {
        return limit == usage;
    }
    
    public void clear() {
        System.out.println("LIXEIRA ESVAZIADA!");
        usage = 0;
    }

    public Long getId() {
        return id;
    }

    public Integer getUsage() {
        return usage;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public Integer getLongitude() {
        return longitude;
    }
    
    public Integer getQuadrant() {
        return getQuadrant(latitude, longitude);
    }
    
    private int getQuadrant(final int y, final int x) {
        return 1;
        /*
        return (y >= 0)
                ? (x >= 0) ? 1 : 2
                : (x >= 0) ? 3 : 4;
        */
    }
    
}
