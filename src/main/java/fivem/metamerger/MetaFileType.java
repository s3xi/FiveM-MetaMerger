package fivem.metamerger;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.List;

import fivem.metamerger.merger.Merger;

public enum MetaFileType {

    CARCOLS ("CVehicleModelInfoVarGlobal", new String[] {"Kits", "Lights"}),
    CARVARIATIONS ("CVehicleModelInfoVariation", new String[] {"variationData"}),
    HANDLING ("CHandlingDataMgr", new String[] {"HandlingData"}),
    VEHICLELAYOUTS ("CVehicleMetadataMgr", new String[] {}), //TODO
    VEHICLES ("CVehicleModelInfo__InitDataList", new String[] {"InitDatas", "txdRelationships"});

    private String nodeId;
    private String[] nodeIds;

    MetaFileType(String nodeId, String[] nodeIds) {
        this.nodeId = nodeId;
        this.nodeIds = nodeIds;
    }

    public String getFileName() {
        return new StringBuilder(name().toLowerCase()).append(".meta").toString();
    }

    public Merger getMerger(List<MetaFile> metaFiles) {
        try {
            Constructor<?> constructor = Merger.class.getConstructor(Array.newInstance(String.class, 0).getClass(), List.class);
            return (Merger) constructor.newInstance(nodeIds, metaFiles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MetaFileType getFileType(MetaFile metaFile) {
        for (MetaFileType metaFileType : values()) {
            if (metaFileType.nodeId.equals(metaFile.getNodeId())) {
                return metaFileType;
            }
        }
        return null;
    }
}
