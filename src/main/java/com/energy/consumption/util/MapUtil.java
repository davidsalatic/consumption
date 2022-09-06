package com.energy.consumption.util;

import com.energy.consumption.model.EntityWithPublicId;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MapUtil {

    public Map<String, EntityWithPublicId> groupByPublicId(List<? extends EntityWithPublicId> entities) {
        Map<String, EntityWithPublicId> result = new HashMap<>();

        for (EntityWithPublicId entity : entities) {
            result.put(entity.getPublicId(), entity);
        }

        return result;
    }
}
