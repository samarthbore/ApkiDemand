# Apki Demand UI Redesign Implementation Plan

This plan details the steps required to completely overhaul the app's UI to match the provided light-theme reference image. The new design shifts from a purple/blue aesthetic to a premium Forest Green and Gold/Mustard color scheme.

## User Review Required

> [!IMPORTANT]
> The reference image shows both a "Demand" bottom tab and a central large `+` Floating Action Button (FAB) in gold. Typically, a large central FAB is used for the primary action (e.g., "Post Demand"), which makes having a separate "Demand" tab somewhat redundant. 
> 
> **My Proposed Approach**: I will implement the central gold FAB as the primary "Post Demand" button, and replace the second bottom tab with something like "Offers" or keep it as "Demand" (acting as a list of "My Demands"). Please let me know if this behavior aligns with your vision!

## Proposed Changes

### 1. Brand Colors Update
The core color tokens will be updated to match the new premium aesthetic.
#### [MODIFY] [Color.kt](file:///c:/ApkiDemand/ApkiDemandApp/core/ui/src/main/java/com/apkidemand/core/ui/theme/Color.kt)
- Update `md_theme_light_primary` to Forest Green (`#166534`).
- Update `md_theme_light_secondary` to Gold/Mustard (`#C2883A`).
- Update `md_theme_dark_primary` and `md_theme_dark_secondary` to corresponding dark-mode friendly variants.

---

### 2. Custom Bottom Navigation & Main Shell
The standard bottom navigation will be replaced with a custom layout to support the overlapping, centered FAB.
#### [MODIFY] [MainShell.kt](file:///c:/ApkiDemand/ApkiDemandApp/feature/home/src/main/java/com/apkidemand/feature/home/MainShell.kt)
- Implement `BottomAppBar` with a clean white background.
- Integrate a centered `FloatingActionButton` (FAB) using the new Gold color (`#C2883A`) with a white `+` icon for the primary posting action.
- Ensure the active tab indicator uses the Forest Green color, matching the "Feed" tab in the reference image.

---

### 3. Home Feed Screen Overhaul
The layout will be carefully adjusted to be a 1:1 match with the provided mockup.
#### [MODIFY] [HomeFeedScreen.kt](file:///c:/ApkiDemand/ApkiDemandApp/feature/home/src/main/java/com/apkidemand/feature/home/HomeFeedScreen.kt)
- **Hero Banner**: Redesign to use a clean white card with a subtle border. Add the "Post local shopkeepers..." text and the Gold "+ Post Your Demand" button.
- **Category Chips**: Set the "All" chip to be filled with Forest Green and a white `🔥` emoji. The rest of the chips will be light gray.
- **Section Headers**: Add the "6 Active" badge and "Sort by Popular" dropdown text.
- **Offer Cards**: 
  - Ensure the price section uses Gold for the primary price and strikethrough gray for the original price.
  - Update the "Negotiable" badge to a soft green background.
  - Update the "View Details ->" button to a filled Forest Green button.

---

### 4. System-Wide Redesign Alignment
Update all remaining screens to strip out the old purple/blue gradients and apply the new aesthetic.
#### [MODIFY] [ChatsScreen.kt](file:///c:/ApkiDemand/ApkiDemandApp/feature/home/src/main/java/com/apkidemand/feature/home/ChatsScreen.kt)
#### [MODIFY] [ChatDetailScreen.kt](file:///c:/ApkiDemand/ApkiDemandApp/feature/home/src/main/java/com/apkidemand/feature/home/ChatDetailScreen.kt)
#### [MODIFY] [ProfileScreen.kt](file:///c:/ApkiDemand/ApkiDemandApp/feature/home/src/main/java/com/apkidemand/feature/home/ProfileScreen.kt)
#### [MODIFY] [PostDemandScreen.kt](file:///c:/ApkiDemand/ApkiDemandApp/feature/post-demand/src/main/java/com/apkidemand/feature/postdemand/PostDemandScreen.kt)
- Replace any remaining gradient backgrounds with clean surface variants or solid primary/secondary colors.
- Ensure buttons and interactive elements use the new Green/Gold pairing.

## Verification Plan
### Manual Verification
- Render the `HomeFeedScreen` to verify visual parity with the provided reference image.
- Test the new Bottom Navigation Bar to ensure the FAB overlay works correctly and doesn't block underlying content.
- Navigate through all tabs to ensure the color theme has been applied consistently without breaking the UI.
