# Instructions on how to deploy code

## Open up VS Code for WPILib and connect it to this folder 

CTRL + K + O the file path "C:\\Users\\<you>\\source\\repo\\LC2021"

source should hypothetically be pinned

## Retrieve the most recent code

In VS Code type Ctrl + ~ (located above the tab)
In the box that popped up type the following

```bash
git pull
```

It shouldn't ask for a login but we will generate a generic one

```bash
Login:
robotics@lansdalecatholic.com
<same_as_the_account>
```

## Build Code

In VS Code type Ctrl + SHIFT + P 

In the dialogue box that popped up type WPILib: Build Robot Code

## Deploy Code

While Connected to the robot through WiFi, Ethernet, or USB

In VS Code type Ctrl + SHIFT + P 

In the dialogue box that popped up type WPILib: Deploy Robot Code