package org.tim_18.UberApp.dto.paginatorDTO;

import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
public class FindAllDTO<T> {
    private long totalCount;
    private Collection<T> objects;

    public FindAllDTO() {}
    public FindAllDTO(List<T> objs) {
        this.objects    = objs;
        this.totalCount = objs.size();
    }
    public FindAllDTO(Set<T> objs) {
        this.objects    = objs;
        this.totalCount = objs.size();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("totalCount:");
        sb.append(Long.toString(totalCount));
        sb.append(",\n");
        sb.append("results: [\n");
        for (Object obj : objects) {
            sb.append("{\n");
            sb.append(obj.toString());
            sb.append("\n}\n");
        }
        sb.append("]");
        sb.append("}");
        return sb.toString();
    }
}
