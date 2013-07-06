package com.babyduncan.purpleFrog.partview.internal.page;

import java.util.ArrayList;
import java.util.List;

public class PartialViewPath {

    private final PartialView owner;

    private String fullPath;

    private final List<PartialView> fullPart = new ArrayList<PartialView>(5);

    public PartialViewPath(PartialView owner) {
        this.owner = owner;
    }


    public int getSize() {
        return this.fullPart.size();
    }

    public String getFullPath() {
        return fullPath;
    }


    public void addPartialViewNode(final PartialView node) {
        this.fullPart.add(node);
    }


    public void buildPath() {
        StringBuilder sb = new StringBuilder();
        final int size = fullPart.size();
        for (int i = 0; i < size; i++) {
            sb.append(fullPart.get(i).getName());
            if (i < size - 1) {
                sb.append("-");
            }
        }
        this.fullPath = sb.toString();
    }


    public PartialView get(int index) {
        return this.fullPart.get(index);
    }


    public List<PartialView> getFullPart() {
        return fullPart;
    }

    PartialView getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "PartialViewPath{" +
                "owner=" + owner +
                ", fullPath='" + fullPath + '\'' +
                ", fullPart=" + fullPart +
                '}';
    }
}
