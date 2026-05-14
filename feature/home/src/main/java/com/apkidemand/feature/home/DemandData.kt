package com.apkidemand.feature.home

import androidx.compose.ui.graphics.Color

// ── Buyer sees: Seller Offers (deals nearby) ──────────────
data class OfferItem(
    val id: String,
    val shopName: String,
    val shopInitials: String,
    val sellerName: String,
    val title: String,
    val description: String,
    val category: String,
    val categoryEmoji: String,
    val price: Int,
    val originalPrice: Int?,
    val discountBadge: String?,
    val isNegotiable: Boolean,
    val areaName: String,
    val cityName: String,
    val distance: String,
    val rating: Double,
    val totalOffers: Int,
    val imageUrl: String,
    val timeAgo: String,
    val isVerified: Boolean
)

// ── Seller sees: Buyer Demands (what people need) ─────────
data class DemandItem(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val categoryEmoji: String,
    val buyerName: String,
    val buyerInitials: String,
    val areaName: String,
    val cityName: String,
    val budgetMin: Int?,
    val budgetMax: Int?,
    val offerCount: Int,
    val isUrgent: Boolean,
    val timeAgo: String,
    val photoCount: Int
)

// ── Category Filters ──────────────────────────────────────
val categoryFilters = listOf(
    "All" to "🔥", "Fashion" to "👗", "Electronics" to "📱",
    "Food" to "🎂", "Services" to "🔧", "Grocery" to "🍯",
    "Education" to "📚", "Health" to "💊", "Home" to "🏠"
)

// ── Sample Offers (shown to Buyers) ───────────────────────
// TODO: DEMO DATA - Do not use in production. Ensure UI uses real data from Firestore.
val sampleOffers = listOf(
    OfferItem("o1", "Rajesh Silks", "RS", "Rajesh G.", "Banarasi Silk Saree Collection", "Pure silk sarees in all colors, zari work, wedding special. Walk-in & delivery available.", "Fashion", "👗", 4500, 6000, "Negotiable", false, "Rajapeth", "Amravati", "0.8 km", 4.5, 128, "https://images.unsplash.com/photo-1610189013233-030cdb46a2ee?auto=format&fit=crop&q=80&w=400", "1h ago", true),
    OfferItem("o2", "TechZone Mobiles", "TZ", "Vikram S.", "iPhone Cases & Accessories", "Premium silicon, leather & rugged cases. All models available. Same-day delivery.", "Electronics", "📱", 299, 599, "20% OFF", false, "Camp Area", "Amravati", "1.2 km", 4.2, 96, "https://images.unsplash.com/photo-1603313011101-320f26a4f6f6?auto=format&fit=crop&q=80&w=400", "3h ago", true),
    OfferItem("o3", "Cake Corner", "CC", "Meena D.", "Custom Birthday Cakes", "Fresh, eggless & customized cakes for every celebration. Order now!", "Food", "🎂", 800, null, null, false, "Rajapeth", "Amravati", "0.6 km", 4.6, 210, "https://images.unsplash.com/photo-1558961363-fa8fdf82db35?auto=format&fit=crop&q=80&w=400", "2h ago", true),
    OfferItem("o4", "Fix-It-All Services", "FA", "Santosh K.", "Carpenter & Furniture Work", "Wardrobes, kitchen cabinets, TV units. Sunmica & laminate finish. 15 years exp.", "Services", "🔧", 5000, null, "Negotiable", true, "Nandgaon", "Amravati", "3.4 km", 4.3, 67, "https://images.unsplash.com/photo-1581141849291-1125c7b692b5?auto=format&fit=crop&q=80&w=400", "5h ago", true),
    OfferItem("o5", "Nature's Best", "NB", "Priya T.", "Organic Honey & Dry Fruits", "Pure unprocessed honey, almonds, cashews, walnuts. Farm-sourced, lab-tested.", "Grocery", "🍯", 350, 500, null, false, "Badnera Rd", "Amravati", "2.1 km", 4.6, 43, "https://images.unsplash.com/photo-1587049352847-4d4b122858f9?auto=format&fit=crop&q=80&w=400", "4h ago", false),
    OfferItem("o6", "Guruji Classes", "GC", "Dr. Amit P.", "CBSE/ICSE Math & Science", "Class 8-12 tuition. Board exam specialist. Small batches. Demo class free.", "Education", "📚", 2000, 3000, "10% OFF", false, "VMV Road", "Amravati", "1.8 km", 4.9, 156, "https://images.unsplash.com/photo-1427504494785-319ce5154564?auto=format&fit=crop&q=80&w=400", "6h ago", true),
)

// ── Sample Demands (shown to Sellers) ─────────────────────
// TODO: DEMO DATA - Do not use in production. Ensure UI uses real data from Firestore.
val sampleDemands = listOf(
    DemandItem("d1", "Red Banarasi Silk Saree", "Looking for pure Banarasi silk in maroon/red with gold zari work for wedding", "Fashion", "👗", "Priya S.", "PS", "Rajapeth", "Amravati", 3000, 8000, 5, true, "2h ago", 2),
    DemandItem("d2", "iPhone 14 Back Cover", "Need a transparent silicon case for iPhone 14, anti-yellow preferred", "Electronics", "📱", "Rahul K.", "RK", "Camp Area", "Amravati", 200, 500, 3, false, "4h ago", 1),
    DemandItem("d3", "Birthday Cake 2kg", "Chocolate truffle cake, eggless, with custom name topping for party", "Food", "🎂", "Sneha M.", "SM", "Gadge Nagar", "Amravati", 800, 1500, 8, true, "1h ago", 0),
    DemandItem("d4", "Carpenter for Wardrobe", "Need carpenter to build 6x4 ft wardrobe with mirror. Sunmica finish.", "Services", "🔧", "Amit P.", "AP", "Nandgaon", "Amravati", 8000, 15000, 2, false, "6h ago", 3),
    DemandItem("d5", "Organic Honey 500g", "Looking for pure, unprocessed organic honey. Local brand preferred.", "Grocery", "🍯", "Neha D.", "ND", "Badnera Rd", "Amravati", 300, 600, 4, false, "3h ago", 1),
    DemandItem("d6", "Math Tutor for Class 10", "Need experienced math tutor for CBSE class 10, 3 days/week", "Education", "📚", "Suresh W.", "SW", "VMV Road", "Amravati", 1500, 3000, 6, false, "5h ago", 0),
)
