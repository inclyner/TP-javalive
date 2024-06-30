# TP PA-JAVALIFE

This project implements a simulation ecosystem where various elements interact based on predefined rules and configurations. The ecosystem's graphical representation scales based on a defined ecosystem size and screen resolution, with each ecosystem unit corresponding to 2 pixels on a 1000-pixel wide screen.

## Project Structure

The application is structured into packages as follows:

- `pt.isec.pa.javalife`: Root package covering the entire application.
- `pt.isec.pa.javalife.model`: Includes the facade class accessing the core logic.
- `pt.isec.pa.javalife.model.data`: Manages data structures.
- `pt.isec.pa.javalife.model.gameengine`: Handles the tick generator for simulation.
- `pt.isec.pa.javalife.ui.gui`: Implements the graphical user interface using JavaFX.
- `pt.isec.pa.javalife.model.command`
- `pt.isec.pa.javalife.model.memento`
- `pt.isec.pa.javalife.ui.gui.res` (for multimedia resources management).

## General Requirements

- Clear separation between UI and implementation.
- Application of scaling based on ecosystem size.
- Simulation speed variability.
- Single subtype assumption for each main type (Rock, Grass, Animal).
- Configuration options active during simulation configuration or when paused.
- Use of `Ecossistema` class to manage simulation elements.
- Unique identifier for each element type.
- Implementation of a simplified clock tick generator.
- `Facade` class with JavaDoc-compatible comments.
- Optional: Unit tests for FSM or Command patterns.

## Software Design Patterns

Implemented design patterns within the project:

- Serialization: Save and load simulations.
- Command (UI operations like add, remove elements, configuration).
- FSM (Fauna state management during simulation).
- Factory (Element creation).
- Memento (Snapshot creation and restoration).
- Facade with observer functionalities.
- Multiton (Image management for fauna elements).

## Ecosystem Elements

### Inanimate

- Fixed elements in the simulation area.
- Surrounding barrier preventing animated elements from leaving.
- Rectangular shapes of various sizes.

### Flora

- Non-moving elements with reproduction capability based on strength.
- Strength ranges from 0 to 100.
- Reproduction at strength 90, reducing parent's strength to 60.
- Adjacency defined for reproduction and interaction with fauna.
- Strength decay when overlapped by fauna.
- Represented with customizable color and transparency.

### Fauna

- Moving elements with speed and direction.
- Strength ranges from 0 to 100.
- Movement decreases strength.
- Forage when strength below 35.
- Attack other fauna or feed on flora.
- Reproduction at strength 50, reducing parent's strength by 25.

## GUI Application

Implemented using JavaFX:

- Application class receives and propagates `Facade` reference.
- Menu functionalities for file operations, ecosystem configurations, and simulation controls.

### Menu Options

- File: Create, Open, Save, Export, Import, Exit.
- Ecossistema: General settings, Add elements, Edit/Delete elements, Undo/Redo.
- Simulation: Configuration, Start/Stop, Pause/Resume, Save/Restore snapshot.
- Events: Sun application, Herbicide injection, Strength injection.

---

This README provides an overview of the TP PA-JAVALIFE project, its structure, implemented design patterns, ecosystem elements, and GUI functionalities.

