# Fitts' Law

This repository contains an implementation and analysis of **Fitts' Law**, a predictive model of human movement primarily used in ergonomics and human-computer interaction (HCI). Fitts' Law models the relationship between the size of a target, the distance to that target, and the time it takes to reach the target.

## Overview

Fitts' Law states that the time required to move to a target is a function of the ratio between the distance to the target and the width of the target. This law can be expressed by the following equation:

```
T = a + b * log2(2D / W)
```

Where:
- **T** is the average time to complete the movement.
- **D** is the distance from the starting point to the center of the target.
- **W** is the width of the target along the axis of motion.
- **a** and **b** are empirically determined constants.

This implementation allows you to experiment with various target sizes and distances to better understand how Fitts' Law works.

## Features
- Interactive visualization to demonstrate Fitts' Law in practice.
- Customizable target distance and width.
- Automatic computation of movement time based on user input.
- Analysis of user performance with visual feedback.

## Installation

1. **Clone the repository**:

    ```bash
    git clone https://github.com/ilokeshmeena/Fitts-Law.git
    cd Fitts-Law
    ```
2. **Run the application**:

    ```bash
    Android Studio
    ```

## Usage

1. Launch the application, and you'll be presented with a series of targets.
2. Try clicking on the targets as quickly as possible.
3. The program will measure the time it takes to hit each target.
4. It will then provide feedback and analysis of the movement time based on the Fitts' Law model.

## Contributing

Contributions are welcome! If you'd like to contribute to this project, please fork the repository, create a new branch, and submit a pull request. Ensure that your code follows the best practices and includes tests where appropriate.

1. Fork the project.
2. Create your feature branch (`git checkout -b feature/new-feature`).
3. Commit your changes (`git commit -m 'Add new feature'`).
4. Push to the branch (`git push origin feature/new-feature`).
5. Open a pull request.
