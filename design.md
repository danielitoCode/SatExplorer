# SatExplorer — Complete UI/UX Design Specification

## Project Overview

SatExplorer is a modern Android application focused on real-time satellite tracking, orbital visualization, and aerospace-grade mobile visualization.

The application combines:

- Jetpack Compose
- OpenGL ES rendering
- Real satellite orbital mechanics (SGP4)
- Modern mission-control style interfaces
- Interactive 3D Earth visualization
- Satellite data from CelesTrak

The visual identity should feel like a combination of:

- NASA mission control
- SpaceX telemetry systems
- Modern cyber-minimal UI
- Scientific aerospace software
- Premium Android flagship applications

The application must feel:

- Technical
- Futuristic
- Clean
- Lightweight
- Professional
- Data-driven
- Responsive
- GPU accelerated

---

# Global Design Language

## Design Philosophy

The UI should prioritize:

1. Information density without clutter
2. Smooth animations
3. High contrast readability
4. Minimalistic scientific aesthetics
5. Aerospace telemetry visual style
6. Glassmorphism only in subtle amounts
7. Deep-space visual atmosphere

---

# Color Palette

## Primary Background

```text
Deep Space Black: #05070D
```

Used for:
- Main backgrounds
- 3D view background
- Navigation surfaces

---

## Secondary Background

```text
Midnight Navy: #0A1220
```

Used for:
- Cards
- Panels
- Bottom sheets
- Overlays

---

## Surface Color

```text
Space Gray: #131C2E
```

Used for:
- Elevated components
- Containers
- Dialogs

---

## Primary Accent

```text
Electric Cyan: #00D9FF
```

Used for:
- Buttons
- Orbital paths
- Selected items
- Interactive highlights
- Active satellite markers

---

## Secondary Accent

```text
Orbital Blue: #3D7BFF
```

Used for:
- Navigation indicators
- Progress indicators
- Secondary actions

---

## Warning Accent

```text
Signal Amber: #FFC857
```

Used for:
- Collision alerts
- Warnings
- Sensor limitations

---

## Danger Accent

```text
Telemetry Red: #FF4C61
```

Used for:
- Errors
- Connection failures
- Tracking loss

---

## Success Accent

```text
Signal Green: #31E981
```

Used for:
- Successful sync
- Satellite lock acquired
- Confirmations

---

## Text Colors

### Primary Text

```text
#F5F7FA
```

### Secondary Text

```text
#A6B1C2
```

### Disabled Text

```text
#5B6475
```

---

# Typography

## Recommended Fonts

### Primary Font

Use:

```text
Inter
```

Alternative:

```text
Manrope
```

---

## Numeric / Telemetry Font

Use:

```text
JetBrains Mono
```

Used for:
- Coordinates
- Altitude
- Velocity
- Orbital data
- Telemetry readouts
- Timestamps

---

# Typography Scale

## Display Large

```text
40sp
Bold
```

## Headline Large

```text
32sp
SemiBold
```

## Headline Medium

```text
26sp
SemiBold
```

## Title Large

```text
22sp
Medium
```

## Title Medium

```text
18sp
Medium
```

## Body Large

```text
16sp
Regular
```

## Body Medium

```text
14sp
Regular
```

## Label Small

```text
12sp
Medium
```

## Telemetry Values

```text
14sp
JetBrains Mono
Medium
```

---

# Global UI Rules

## Corner Radius

### Cards

```text
20dp
```

### Buttons

```text
16dp
```

### Sheets

```text
28dp top corners
```

---

## Shadows

Use soft shadows only.

Avoid:
- harsh shadows
- neumorphism
- excessive blur

---

## Blur Effects

Use subtle blur:

```text
12dp max blur
```

Only for:
- floating panels
- HUD overlays
- dialogs

---

# Navigation Structure

## Main Navigation Type

Use:

```text
Bottom Navigation Bar
```

with:

5 primary destinations.

---

# Main Pages

## 1. Home Dashboard

Purpose:
Mission control overview.

---

### Components

#### Top App Bar

Contains:
- profile avatar
- greeting
- current UTC time
- sync status

---

### Hero Section

Animated miniature Earth visualization.

Displays:
- active tracked satellites
- ISS visibility
- nearby passes

---

### Quick Stats Cards

Cards include:

- Satellites tracked
- Objects visible tonight
- Active orbit calculations
- Last sync time

---

### Live Activity Feed

Shows:
- new passes
- conjunction alerts
- telemetry updates
- atmospheric warnings

---

### Quick Actions

Buttons:

- Open Globe
- Track ISS
- AR Sky View
- Favorites
- Search Satellite

---

## 2. 3D Globe Screen

Purpose:
Core OpenGL ES visualization.

---

### Fullscreen Globe

Features:
- rotating Earth
- orbital paths
- satellite markers
- atmosphere glow
- day/night shading
- starfield background

---

### Floating HUD Overlay

Top Left:
- FPS
- satellites rendered
- current orbit count

Top Right:
- compass
- zoom level
- orientation mode

Bottom:
- selected satellite data panel

---

### Gesture Controls

Support:
- pinch zoom
- rotate globe
- tilt
- double tap focus
- satellite selection

---

### Satellite Detail Sheet

Contains:
- satellite name
- NORAD ID
- altitude
- speed
- orbit type
- inclination
- launch date
- country
- visibility probability

---

## 3. Satellite Search Screen

Purpose:
Search and filter satellites.

---

### Search Bar

Supports:
- name search
- NORAD ID search
- category filtering

---

### Filter Chips

Categories:

- ISS
- Starlink
- Weather
- GPS
- Military
- Amateur Radio
- Science
- Debris

---

### Satellite Cards

Each card contains:

- satellite icon
- orbit type
- altitude
- live status
- visibility indicator
- favorite button

---

## 4. AR Sky Tracker

Purpose:
Point phone toward sky to locate satellites.

---

### Camera Background

Uses CameraX.

---

### Overlay Elements

- satellite markers
- trajectory lines
- labels
- direction arrows
- elevation angle
- distance

---

### Sensor Indicators

Displays:
- calibration quality
- compass state
- gyro state

---

### Detection Modes

Modes:
- Visible only
- ISS only
- Brightest satellites
- Favorites

---

## 5. Profile Screen

Purpose:
User settings and personalization.

---

### Profile Header

Contains:
- profile photo
- username
- user rank
- account level
- tracked satellites count

---

### User Stats

Cards:
- total observations
- favorite satellites
- hours tracked
- night sessions

---

### Preferences

Settings:
- dark mode
- telemetry units
- orbital refresh rate
- AR mode settings
- notifications
- cache size

---

### Account Management

Buttons:
- Edit profile
- Security
- Sync account
- Export data
- Logout

---

# Authentication Flow

## Launch Flow

```text
Splash Screen
→ Onboarding
→ Authentication
→ Dashboard
```

---

# Splash Screen

## Visual Style

- animated Earth
- orbital rings
- subtle stars
- glowing cyan logo

---

## Animation

Sequence:

1. Logo fade in
2. Orbital ring animation
3. Earth glow activation
4. App title reveal

Duration:

```text
2.5 seconds
```

---

# Onboarding

## Total Screens

```text
3 screens
```

---

## Screen 1

Title:

```text
Track Earth In Real Time
```

Visual:
3D Earth with orbiting satellites.

---

## Screen 2

Title:

```text
Explore The Sky With AR
```

Visual:
Phone pointing toward satellites.

---

## Screen 3

Title:

```text
Real Orbital Mechanics
```

Visual:
Telemetry overlays and orbital paths.

---

## Bottom Controls

Buttons:
- Skip
- Next
- Get Started

Indicators:
- animated progress dots

---

# Login Screen

## Layout

Centered vertical layout.

---

### Components

#### App Logo

Animated orbital symbol.

---

#### Title

```text
Welcome Back
```

---

#### Email Field

Rounded modern text field.

---

#### Password Field

Includes:
- visibility toggle
- validation

---

#### Login Button

Full width.

Accent color:

```text
Electric Cyan
```

---

#### Social Login

Buttons:
- Google
- GitHub

---

#### Footer

```text
Don't have an account? Sign Up
```

---

# Registration Screen

## Fields

- Username
- Email
- Password
- Confirm Password

---

## Validation Rules

Show real-time validation.

Include:
- password strength meter
- email validation
- username availability

---

## CTA

```text
Create Account
```

---

# Forgot Password Screen

Minimalistic.

Contains:
- email field
- reset button
- success state

---

# Edit Profile Screen

## Editable Fields

- avatar
- display name
- bio
- country
- favorite satellite category

---

# Notification Center

## Sections

- Pass Alerts
- ISS Visibility
- Conjunction Warnings
- News
- Sync Failures

---

# Settings Screen

## Categories

### Appearance

- Theme mode
- Accent color
- UI density
- Globe quality

---

### Tracking

- refresh interval
- cache duration
- offline mode
- update frequency

---

### Performance

- FPS limit
- texture resolution
- render distance
- orbit density

---

### Privacy

- telemetry sharing
- analytics
- location usage

---

# Search Experience

## Search Behavior

Must support:

- instant suggestions
- recent searches
- trending satellites
- fuzzy matching

---

# Animations

## General Rules

All animations must feel:

- smooth
- scientific
- elegant
- responsive

Avoid:
- playful/cartoon animations
- overshooting springs
- excessive bounce

---

## Animation Durations

### Fast

```text
150ms
```

### Medium

```text
300ms
```

### Slow

```text
600ms
```

---

# OpenGL Visual Style

## Earth Rendering

Features:
- realistic Earth texture
- atmosphere glow
- night lights
- cloud layer
- soft specular highlights

---

## Satellite Rendering

Use:
- emissive markers
- orbit trail lines
- dynamic scaling

---

## Orbit Lines

Colors by category:

- ISS → Cyan
- Starlink → Blue
- Weather → Green
- Military → Red
- Debris → Gray

---

# Performance Targets

## Target FPS

```text
60 FPS preferred
30 FPS minimum
```

---

## Memory Budget

```text
< 300 MB RAM preferred
```

---

## APK Size Goal

```text
< 50 MB
```

---

# Accessibility

## Requirements

- scalable text
- contrast compliant
- screen reader support
- large touch targets

---

# Empty States

Every screen must include:

- informative illustration
- helpful message
- recovery CTA

---

# Error States

Errors should feel technical but calm.

Example:

```text
Telemetry sync lost.
Attempting reconnection...
```

Avoid generic:

```text
Something went wrong
```

---

# Offline Mode

The app should support:

- cached TLE data
- last known satellite positions
- offline globe rendering

---

# Recommended Compose Architecture

## UI Layers

- Screens
- Components
- Design System
- Theme
- Navigation
- OpenGL Container
- Overlays

---

# Recommended Component Library

Create reusable components:

- SatelliteCard
- OrbitChip
- TelemetryPanel
- GlobeOverlay
- StatusBadge
- SignalIndicator
- OrbitalButton
- SpaceTextField

---

# Final Experience Goal

The final product should feel like:

```text
A professional aerospace tracking system redesigned for modern Android flagship devices.
```

The application should impress:

- recruiters
- Android engineers
- graphics programmers
- aerospace enthusiasts
- UI/UX designers
- systems engineers

while remaining lightweight, smooth, and scientifically grounded.

