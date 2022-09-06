package com.energy.consumption.util;

import com.energy.consumption.model.EntityWithPublicId;
import com.energy.consumption.profile.model.Profile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MapUtilUnitTests {

    @InjectMocks
    private MapUtil mapUtil;

    private static final String ENTITY_1_PUBLIC_ID = "group1";
    private static final String ENTITY_2_PUBLIC_ID = "group2";

    @Test
    void groupByPublicId_successfully() {
        Profile entityFromGroup1 = new Profile();
        entityFromGroup1.setPublicId(ENTITY_1_PUBLIC_ID);

        Profile entityFromGroup2 = new Profile();
        entityFromGroup2.setPublicId(ENTITY_2_PUBLIC_ID);

        Map<String, EntityWithPublicId> groupedEntities = mapUtil.groupByPublicId(List.of(entityFromGroup1, entityFromGroup2));

        assertEquals(2, groupedEntities.size());

        assertEquals(entityFromGroup1, groupedEntities.get(ENTITY_1_PUBLIC_ID));
        assertEquals(entityFromGroup2, groupedEntities.get(ENTITY_2_PUBLIC_ID));
    }
}
