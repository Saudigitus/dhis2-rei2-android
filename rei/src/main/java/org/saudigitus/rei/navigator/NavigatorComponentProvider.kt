package org.saudigitus.rei.navigator

interface NavigatorComponentProvider {
    val lineListing: LineListingComponentProvider
    val teiDashboard: TeiDashboardComponentProvider
}
