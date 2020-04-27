Card Controllers
=====================================================================
0 Simple Player
---------------------------------------------------------------------
**Move Filter:**

- Dome 
- MyWorkers
- OtherWorkers
 - Up By 2

**Build Filter:**

- Dome
 - MyWorkers
 - OtherWorkers

**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|------|-----|
| 1 |Simple Move|Simple Build| | | null|
| 2 | Simple Build| Simple Move| | |null| |TRUE|

1 Apollo
---------------------------------------------------------------------

**Move Filter:**

- Dome 
- MyWorkers
- Up By 2

**Build Filter:**

- Dome
- MyWorkers
- OtherWorkers
 
**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|---|-----|
| 1 |Swap Move|Simple Build|  |  | null||
| 2 | Simple Build| Swap Move|  |  |null| |TRUE|


2 Artemis
---------------------------------------------------------------------

**Move Filter:**

- Dome 
- MyWorkers
- OtherWorkers
 - Up By 2

**Build Filter:**

- Dome
- MyWorkers
- OtherWorkers

**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|----|---|
| 1 |Simple Move|Move Build| | OldPosMove(Action) | null|
| 2 | Move Build| Simple Move| | |Build| | TRUE|
| 3 | Move Build| Simple Build| |   |Move|
| 4 | Simple Build| Simple Move|  |  |null| |TRUE|


3 Athena
---------------------------------------------------------------------

**Move Filter:**

- Dome 
- MyWorkers
- OtherWorkers
 - Up By 2

**Build Filter:**

- Dome
- MyWorkers
- OtherWorkers

**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|------|-----|
| 1 |Simple Move|Simple Build|  | Opp.NotUp(Action)| null|
| 2 | Simple Build| Simple Move|  | |null| |TRUE|

4 Atlas
---------------------------------------------------------------------

**Move Filter:**

- Dome 
- MyWorkers
- OtherWorkers
 - Up By 2

**Build Filter:**

- Dome
- MyWorkers
- OtherWorkers

**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|------|-----|
| 1 |Simple Move|Dome Build|  | | null|
| 2 | Dome Build| Simple Move|  |  |null| |TRUE|

5 Demeter
---------------------------------------------------------------------

**Move Filter:**

- Dome 
- MyWorkers
- OtherWorkers
 - Up By 2

**Build Filter:**

- Dome
- MyWorkers
- OtherWorkers

**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|------|-----|
| 1 |Simple Move|Simple Build|  |  | null|
| 2 |Simple Build|Second Build|  OldBuild(Action)| | null | TRUE
| 3 | Second Build| Simple Move|  | |null | |TRUE|

6 Hephaesus
---------------------------------------------------------------------

**Move Filter:**

- Dome 
- MyWorkers
- OtherWorkers
 - Up By 2

**Build Filter:**

- Dome
- MyWorkers
- OtherWorkers

**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|------|-----|
| 1 |Simple Move|Simple Build|  | | null|
| 2 |Simple Build|Second Build| AllButOld(action), TopLevel|  | null | TRUE
| 3 | Second Build| Simple Move| |  |null | |TRUE|

8 Minotaur
---------------------------------------------------------------------

**Move Filter:**

- Dome 
- MyWorkers
- Up By 2

**Build Filter:**

- Dome
- MyWorkers
- OtherWorkers

**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|---|-----|
| 1 |Push Move|Simple Build|  |  | null||
| 2 | Simple Build| Push Move|  |  |null| |TRUE|

9 Pan
---------------------------------------------------------------------

**Move Filter:**

- Dome 
- MyWorkers
- OtherWorkers
 - Up By 2

**Build Filter:**

- Dome
- MyWorkers
- OtherWorkers 

**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|---|-----|
| 1 |Simple Move|Simple Build|  |  | null||
| 2 | Simple Build| Simple Move|  | |null| |TRUE|

**Victory Condition:**
if (Move Down by two)

10 Prometheus
---------------------------------------------------------------------

**Move Filter:**

- Dome 
- MyWorkers
- OtherWorkers
 - Up By 2

**Build Filter:**

- Dome
- MyWorkers
- OtherWorkers 

**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|----|---|
| 1 | Move Build| Simple Build| |   |Move|
| 2 | Move Build| Simple Move|   | Up By 1 |Build|
| 3 |Simple Move|Simple Build| |   |null|
| 4 | Simple Build| Move Build|  |  |null| |TRUE|


Advanced Gods
========================================================================================

Aphrodite
---------------------------------------------------------------------
Ares
---------------------------------------------------------------------
Bia
---------------------------------------------------------------------
Chaos
---------------------------------------------------------------------
Charon
---------------------------------------------------------------------
Chrouns
----------------------------------------------------------------------
Circe
----------------------------------------------------------------------
Dionysus
----------------------------------------------------------------------
Eros
----------------------------------------------------------------------
Hera
----------------------------------------------------------------------
Hestia
----------------------------------------------------------------------
Hypnus
----------------------------------------------------------------------
Limus
----------------------------------------------------------------------
Medusa
----------------------------------------------------------------------
Morpheus
----------------------------------------------------------------------
Persephone
----------------------------------------------------------------------
Poseidon
----------------------------------------------------------------------
Selene
----------------------------------------------------------------------
Triton
----------------------------------------------------------------------
Zeus
----------------------------------------------------------------------

