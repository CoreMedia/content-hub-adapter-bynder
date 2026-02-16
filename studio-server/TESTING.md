# Running Integration Tests

## Bynder Service Integration Tests

The `BynderServiceIT` integration test requires valid Bynder API credentials to run.

### Option 1: Using System Properties (Recommended for CI/CD)

Run the tests with Maven and pass the credentials as system properties:

```bash
mvn test -Dbynder.apiEndpoint=https://your-instance.bynder.com/api/v4/ -Dbynder.accessToken=your-access-token
```

Or run a specific test:

```bash
mvn test -Dtest=BynderServiceIT -Dbynder.apiEndpoint=https://your-instance.bynder.com/api/v4/ -Dbynder.accessToken=your-access-token
```

### Option 2: Using Properties File (Recommended for Local Development)

1. Copy the template file:
   ```bash
   cd studio-server/src/test/resources
   cp bynder-test.properties.template bynder-test.properties
   ```

2. Edit `bynder-test.properties` and add your credentials:
   ```properties
   bynder.apiEndpoint=https://your-instance.bynder.com/api/v4/
   bynder.accessToken=your-access-token
   ```

3. Run the tests normally:
   ```bash
   mvn test
   ```

**Note:** The `bynder-test.properties` file is in `.gitignore` and will not be committed to version control, keeping your credentials safe.

### Configuration Priority

The test loads credentials in the following order:
1. System properties (`-Dbynder.apiEndpoint` and `-Dbynder.accessToken`)
2. Properties file (`bynder-test.properties`)

System properties take precedence over the properties file.

