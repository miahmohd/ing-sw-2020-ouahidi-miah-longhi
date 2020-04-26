Card Controllers
=====================================================================
0 Simple Player
---------------------------------------------------------------------

**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|------|-----|
| 1 |Simple Move|Simple Build| Dome Workers| N.D.| null|
| 2 | Simple Build| Simple Move| N.D.|Dome, Workers, Up By 2 |null| |TRUE|

1 Apollo
---------------------------------------------------------------------

**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|---|-----|
| 1 |Swap Move|Simple Build| Dome Workers| N.D.| null||
| 2 | Simple Build| Swap Move| N.D.|Dome, My Worker, Up By 2 |null| |TRUE|


2 Artemis
---------------------------------------------------------------------


**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|----|---|
| 1 |Simple Move|Move Build| Dome Workers|Dome, Workers, Up By 2, OldPos(Action) | null|
| 2 | Move Build| Simple Move| N.D.|Dome, My Worker, Up By 2 |Build| | TRUE|
| 3 | Move Build| Simple Build|Dome Workers| N.D. |Move|
| 4 | Simple Build| Simple Move| N.D.|Dome, Workers, Up By 2 |null| |TRUE|


3 Athena
---------------------------------------------------------------------

**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|------|-----|
| 1 |Simple Move|Simple Build| Dome Workers| Opp.NotUp(Action)| null|
| 2 | Simple Build| Simple Move| N.D.|Dome, Workers, Up By 2 |null| |TRUE|

4 Atlas
---------------------------------------------------------------------
**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|------|-----|
| 1 |Simple Move|Dome Build| Dome Workers|Dome, My Worker, Up By 2 | null|
| 2 | Dome Build| Simple Move| N.D.|Dome, Workers, Up By 2 |null| |TRUE|

5 Demeter
---------------------------------------------------------------------
**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|------|-----|
| 1 |Simple Move|Simple Build| Dome Workers| N.D.| null|
| 2 |Simple Build|Second Build| Dome Workers, OldBuild(Action)| N.D.| null | TRUE
| 3 | Second Build| Simple Move| N.D.|Dome, Workers, Up By 2 |null | |TRUE|

6 Hephaesus
---------------------------------------------------------------------
**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|------|-----|
| 1 |Simple Move|Simple Build| Dome Workers| N.D.| null|
| 2 |Simple Build|Second Build| Dome, Workers, AllButOld(action), TopLevel| N.D.| null | TRUE
| 3 | Second Build| Simple Move| N.D.|Dome, Workers, Up By 2 |null | |TRUE|

8 Minotaur
---------------------------------------------------------------------
**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|---|-----|
| 1 |Push Move|Simple Build| Dome Workers| N.D.| null||
| 2 | Simple Build| Push Move| N.D.|Dome, My Worker, Up By 2 |null| |TRUE|

9 Pan
---------------------------------------------------------------------
**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|---|-----|
| 1 |Simple Move|Simple Build| Dome Workers| N.D.| null||
| 2 | Simple Build| Simple Move| N.D.|Dome, Worker, Up By 2 |null| |TRUE|

**Victory Condition:**
if (Move Down by two)

10 Prometheus
---------------------------------------------------------------------

**Transitions:**

|#  |Current State  | Next State    | Build Filter  | Move Filter   |Condition  |Final|LB|
|---|---------------|---------------|---------------|---------------|-----------|----|---|
| 1 | Move Build| Simple Build|Dome Workers| N.D. |Move|
| 2 | Move Build| Simple Move| N.D. |Dome, Workers, Up By 2, Up By 1 |Build|
| 3 |Simple Move|Simple Build|Dome Workers| N.D. |null|
| 4 | Simple Build| Move Build| Dome Workers|Dome, Workers, Up By 2 |null| |TRUE|


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

