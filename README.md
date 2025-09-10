# Install IntelliJ IDEA on Linux

A simple guide to installing IntelliJ IDEA and creating a shortcut command.

## Step 1: Download and Extract IntelliJ IDEA

1. Navigate to a folder for installation, e.g., `~/apps`:

```bash
mkdir -p ~/apps && cd ~/apps
```

2. Download IntelliJ IDEA tarball:

```bash
wget https://download.jetbrains.com/idea/ideaIC-2025.1.1.tar.gz
```

3. Extract the tarball:

```bash
tar -xzf ideaIC-2025.1.1.tar.gz
```

---

## Step 2: Launch IntelliJ IDEA

Run IntelliJ directly from the extracted folder:

```bash
cd idea-IC-*/bin
./idea.sh
```

> Tip: This works, but creating a shortcut command is more convenient.

---

## Step 3: Create a Shortcut Command (`idea`)

1. Open your `.bashrc` file:

```bash
nano ~/.bashrc
```

2. Add this function at the bottom (adjust the path if needed):

```bash
idea() {
    cd ~/apps/idea-IC-*/bin || return
    ./idea.sh &
}
```

3. Save the file:

* Press **Ctrl + O** → Enter

4. Exit nano:

* Press **Ctrl + X**

---

## Step 4: Reload `.bashrc`

```bash
source ~/.bashrc
```

---

## Step 5: Launch IntelliJ Anywhere

Now you can simply type:

```bash
idea
```

IntelliJ IDEA will launch!

---
