# CPU Scheduler

## Table of Contents

- [Overview](#Overview)
- [Algorithms Implemented](#Algorithms_Implemented)
- [Project Structure](#Project_Structure)
- [Installation](#installation)

## Overview
<img width="500" alt="image" src="https://github.com/su0ltan/CPU_scheduler/assets/53498277/dc1dba54-7012-461c-be8d-28fc640b4e96">

This project simulates a multithreading CPU scheduler designed for concurrently manipulating different processes. It was developed as part of the CS227 Operating System course, providing an opportunity to explore various scheduling algorithms.


## Algorithms Implemented
1. **First Come First Serve (FCFS):**
   - Description: FCFS is a simple scheduling algorithm that executes processes in the order they arrive.

2. **Shortest Job First (SJF):**
   - Description: SJF selects the process with the shortest burst time first, minimizing the waiting time for all processes.

3. **Round Robin (RR):**
   - Description: RR is a time-sharing algorithm where each process gets executed in a cyclic order, with a fixed time quantum.


## Project Structure
- `src/`: Contains the source code for the CPU scheduler.
- `doc/`: Documentation files for the project.
- `tests/`: Test cases and sample input files.


## Installation

1. Clone the repository:

```bash
git clone https://github.com/su0ltan/CPU_scheduler.git
cd CPU_scheduler
