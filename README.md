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
- ![image](https://github.com/inclyner/TP-javalive/assets/84443911/e8142742-de53-4176-871a-a4282add5919)

### Flora

- Non-moving elements with reproduction capability based on strength.
- Strength ranges from 0 to 100.
- Reproduction at strength 90, reducing parent's strength to 60.
- Adjacency defined for reproduction and interaction with fauna.
- Strength decay when overlapped by fauna.
- Represented with customizable color and transparency.
- ![image](https://github.com/inclyner/TP-javalive/assets/84443911/4cc5c2b5-aeca-4366-b3d3-d4267b9d6da7)

### Fauna

- Moving elements with speed and direction.
- Strength ranges from 0 to 100.
- Movement decreases strength.
- Forage when strength below 35.
- Attack other fauna or feed on flora.
- Reproduction at strength 50, reducing parent's strength by 25.
![image](https://github.com/inclyner/TP-javalive/assets/84443911/d752b2ac-fb0f-400d-99f3-f0020bc1c23a)

## GUI Application

Implemented using JavaFX:

- Application class receives and propagates `Facade` reference.
- Menu functionalities for file operations, ecosystem configurations, and simulation controls.

### Menu Options

- File: Create, Open, Save, Export, Import, Exit. ![image](https://github.com/inclyner/TP-javalive/assets/84443911/96299677-d831-4825-96dc-64b8f38cb7a2)
- Ecossistema: General settings, Add elements, Edit/Delete elements, Undo/Redo.![image](https://github.com/inclyner/TP-javalive/assets/84443911/d752b2ac-fb0f-400d-99f3-f0020bc1c23a)
- Simulation: Configuration, Start/Stop, Pause/Resume, Save/Restore snapshot.![image](https://github.com/inclyner/TP-javalive/assets/84443911/4cc5c2b5-aeca-4366-b3d3-d4267b9d6da7)
- Events: Sun application, Herbicide injection, Strength injection.![image](https://github.com/inclyner/TP-javalive/assets/84443911/e8142742-de53-4176-871a-a4282add5919)







---

This README provides an overview of the TP PA-JAVALIFE project, its structure, implemented design patterns, ecosystem elements, and GUI functionalities.













![image](https://github.com/inclyner/TP-javalive/assets/84443911/af2d5b27-2d1a-4b2d-94d9-a74d6d327a01)

![image](https://github.com/inclyner/TP-javalive/assets/84443911/79947831-7ad2-4e2e-9396-46833d623e9b)

![image](https://github.com/inclyner/TP-javalive/assets/84443911/2d6d10b4-d02a-43e9-a6e4-8df76ec3ae14)

![image](https://github.com/inclyner/TP-javalive/assets/84443911/644c5ded-5288-466d-841e-4cf112a1bbb8)


![image](https://github.com/inclyner/TP-javalive/assets/84443911/11abe9a4-02cc-461b-be35-08688d4220e3)



