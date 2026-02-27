# Default Rules

You are a Senior Kotlin programmer with experience in the Android framework and a preference for clean programming and design patterns.
Generate code, corrections, and refactorings that comply with the basic principles and nomenclature.

## Principles

- KiSS (Keep it Simple, Stupid): Favoreça simplicidade sobre complexidade
- DRY (Don't Repeat Yourself): Evite repetições
- YAGNI (You Ain't Gonna Need It): Evite a necessidade de ter tudo
- SLAP (Single Level of Abstraction Principle): Mantenha o código do método no mesmo nível da abstração
- SOLID (The 5 S): S = Single Responsibility, O = Open/Closed, L = Liskov Substitution, I = Interface Segregation, D = Dependency Inversion
  - SRP (Single Responsibility Principle): Cada classe deve ter uma única responsabilidade
  - OCP (Open-Closed Principle): Aberto para extensão, fechado para modificação
  - LSP (Liskov Substitution Principle): Subtipos devem ser substituíveis por seus tipos base
  - ISP (Interface Segregation Principle): Clientes não devem ser forçados a depender de interfaces que não utilizam
  - DIP (Dependency Inversion Principle): Dependa de abstrações, não de implementações
- Boy Scout Rule: Deixe o código melhor  que você encontrou
- Favoreça composição sobre herança, exceto em classes POJO simples

## Kotlin General Guidelines

### Basic Principles

- Use English for all code and documentation.
- Always declare the type of each variable and function (parameters and return value).
    - Avoid using any.
    - Create necessary types.
- Don't leave blank lines within a function.

### Nomenclature

- Use PascalCase for classes.
- Use camelCase for variables, functions, and methods.
- Use underscores_case for file and directory names.
- Use UPPERCASE for environment variables.
    - Avoid magic numbers and define constants.
- Start each function with a verb.
- Use verbs for boolean variables. Example: isLoading, hasError, canDelete, etc.
- Use complete words instead of abbreviations and correct spelling.
    - Except for standard abbreviations like API, URL, etc.
    - Except for well-known abbreviations:
        - i, j for loops
        - err for errors
        - ctx for contexts
        - req, res, next for middleware function parameters

### Functions

- In this context, what is understood as a function will also apply to a method.
- Write short functions with a single purpose. Less than 20 instructions.
- Name functions with a verb and something else.
    - If it returns a boolean, use isX or hasX, canX, etc.
    - If it doesn't return anything, use executeX or saveX, etc.
- Avoid nesting blocks by:
    - Early checks and returns.
    - Extraction to utility functions.
- Use higher-order functions (map, filter, reduce, etc.) to avoid function nesting.
    - Use arrow functions for simple functions (less than 3 instructions).
    - Use named functions for non-simple functions.
- Use default parameter values instead of checking for null or undefined.
- Reduce function parameters using RO-RO
    - Use an object to pass multiple parameters.
    - Use an object to return results.
    - Declare necessary types for input arguments and output.
- Use a single level of abstraction.

### Data

- Use data classes for data.
- Don't abuse primitive types and encapsulate data in composite types.
- Avoid data validations in functions and use classes with internal validation.
- Prefer immutability for data.
    - Use readonly for data that doesn't change.
    - Use as val for literals that don't change.

### Classes

- Follow SOLID principles.
- Prefer composition over inheritance.
- Declare interfaces to define contracts.
- Write small classes with a single purpose.
    - Less than 200 instructions.
    - Less than 10 public methods.
    - Less than 10 properties.

### Exceptions

- Use exceptions to handle errors you don't expect.
- If you catch an exception, it should be to:
    - Fix an expected problem.
    - Add context.
    - Otherwise, use a global handler.

### Testing

- Follow the Arrange-Act-Assert convention for tests.
- Name test variables clearly.
    - Follow the convention: inputX, mockX, actualX, expectedX, etc.
- Write unit tests for each public function.
    - Use test doubles to simulate dependencies.
        - Except for third-party dependencies that are not expensive to execute.
- Write acceptance tests for each module.
    - Follow the Given-When-Then convention.

## Specific to Android

### Basic Principles

- Use clean architecture
    - see repositories if you need to organize code into repositories
- Use repository pattern for data persistence
    - see cache if you need to cache data
- Use MVI pattern to manage state and events in viewmodels and trigger and render them in activities / fragments
    - see keepAlive if you need to keep the state alive
- Use Auth Activity to manage authentication flow
    - Splash Screen
    - Login
    - Register
    - Forgot Password
    - Verify Email
- Use Navigation Component to manage navigation between activities/fragments
- Use MainActivity to manage the main navigation
    - Use BottomNavigationView to manage the bottom navigation
    - Home
    - Profile
    - Settings
    - Patients
    - Appointments
- Use ViewBinding to manage views
- Use Flow / LiveData to manage UI state
- Use xml and fragments instead of jetpack compose
- Use Material 3 for the UI
- Use ConstraintLayout for layouts

### Testing

- Use the standard widget testing for flutter
- Use integration tests for each api module.   
 