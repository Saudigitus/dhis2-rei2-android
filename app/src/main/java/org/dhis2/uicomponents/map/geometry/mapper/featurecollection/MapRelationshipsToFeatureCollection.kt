package org.dhis2.uicomponents.map.geometry.mapper.featurecollection

import com.mapbox.geojson.BoundingBox
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.mapboxsdk.geometry.LatLng
import org.dhis2.uicomponents.map.geometry.bound.GetBoundingBox
import org.dhis2.uicomponents.map.geometry.getPointLatLng
import org.dhis2.uicomponents.map.geometry.line.MapLineRelationshipToFeature
import org.dhis2.uicomponents.map.geometry.mapper.addRelationFromInfo
import org.dhis2.uicomponents.map.geometry.mapper.addRelationToInfo
import org.dhis2.uicomponents.map.geometry.mapper.addRelationshipInfo
import org.dhis2.uicomponents.map.geometry.point.MapPointToFeature
import org.dhis2.uicomponents.map.model.RelationshipUiComponentModel

class MapRelationshipsToFeatureCollection(
    private val mapLineToFeature: MapLineRelationshipToFeature,
    private val mapPointToFeature: MapPointToFeature,
    private val bounds: GetBoundingBox
) {
    fun map(relationships: List<RelationshipUiComponentModel>): Pair<Map<String?, FeatureCollection>, BoundingBox> {
        val relationshipByName =
            relationships.groupBy { it.displayName }.mapValues { relationModels ->
                val lineFeatures = relationModels.value.map {
                    val feature = mapLineToFeature.map(it)
                    feature?.addRelationshipInfo(it)
                }
                val pointFromFeatures = relationModels.value.map { relationModel ->
                    relationModel.To.geometry?.let {
                        val feature = mapPointToFeature.map(it)
                        feature?.addRelationFromInfo(relationModel)
                    }
                }
                val pointToFeatures = relationModels.value.map { relationModel ->
                    relationModel.To.geometry?.let {
                        val feature = mapPointToFeature.map(it)
                        feature?.addRelationToInfo(relationModel)
                    }
                }
                listOf(lineFeatures, pointFromFeatures, pointToFeatures).flatten()
            }

        val featuresList = relationshipByName.values.flatten()
        val latLongList = getLngLatAsCoordinateList(featuresList)

        return Pair<Map<String?, FeatureCollection>, BoundingBox>(
            relationshipByName.mapValues { FeatureCollection.fromFeatures(it.value) },
            bounds.getEnclosingBoundingBox(latLongList)
        )
    }

    private fun getLngLatAsCoordinateList(features: List<Feature?>): List<LatLng> {
        return features.map {
            it.getPointLatLng()
        }
    }

    companion object {
        const val FROM_TEI = "fromTeiUid"
        const val TO_TEI = "toTeiUid"
        const val RELATIONSHIP = "relationshipTypeUid"
        const val BIDIRECTIONAL = "bidirectional"
    }
}