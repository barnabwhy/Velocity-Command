# Velocity Command
Adds a server-side command to set the velocity of any entity

### Usage
`/velocity <entity> <velocity> [rotationContext]`

e.g. `/velocity @s ~ 3 ~`

#### Arguments
- `entity`: Entity selector, like vanilla teleport command
- `velocity`: Relative xyz velocity values, also like teleport
- `rotationContext`: When using directional (`^`) coordinates, determines the source of direction: "entity" for the entity's facing direction and "command" for the command context's source (for `/execute facing`), defaults to "entity"
