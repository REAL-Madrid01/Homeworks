# Woodland Creatures Game

# author: Zhiqi Pu
# time: 11/3/2023
# Project: CS5001-p2

## Overview

Woodland Creatures is a Java-based game that simulates a magical forest environment where animals interact with mythical creatures and spells. Players can move animals, cast spells, and aim to achieve victory conditions while avoiding their animals' demise.

## Features

- Animal and Creature interactions
- Spell casting with various effects
- Client-Server communication over TCP/IP
- JSON-based message format for client-server interaction
- Custom game logic with unique animal and creature abilities

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java JDK 11 or later
- Maven (if building the project with Maven)
- Internet connection (for downloading dependencies if not already present)

## Setup

javac -cp "javax.json-1.0.jar:." *.java under src fileRoot

## Run
java -cp "javax.json-1.0.jar:." GameServerMan
- Make sure you use the stacscheck or change port and seed in GameServerMain.java
