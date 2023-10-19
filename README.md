# Restaurant Search Application

This is a Java application developed with SpringBoot that helps users search for local restaurants near their company based on various criteria. The application uses OpenCSV to read data from CSV files (specifically `Cuisine.csv` and `Restaurant.csv`) to make this assessment. All data provided on these files are fake and used only for this assessment.

## Overview
Local restaurants near the company offer a variety of lunch options, but sometimes it can be challenging to decide where to go. This application was created to provide a basic search function to help colleagues find the perfect restaurant for their lunch. 

The search functionality is based on five criterias: 
1. **Restaurant Name:** Search for local restaurants near the company by their exact or partial name;
2. **Customer Rating:** Specify a rating range from 1 star to 5 stars; 
3. **Distance:** Set a range from 1 mile to 10 miles;
4. **Price:** Specify the average cost per person, ranging from $10 to $50;
5. **Cuisine:** Choose from a list of cuisines, including Chinese, American, Thai, and more. 

## Function Rules
- The Restaurant search application allows you to provide up to five parameters based on the criteria listed above, with only one value per parameter. 

- In case you provide any invalid parameter or outside the specified ranges, the application will return an error message.

- For restaurant name and cuisine, if there is no match for the criteria then the application will return an error message.

- In case you don't provide any criteria, an empty list will be presented. But don't hesitate to try again with valid criterias, we can find the perfect restaurant for you! 

- The function returns up to five matches based on the provided criteria. 
    1. If no matches are found, it returns an empty list. 
    2. If less than 5 matches are found, return them all. 
    3. If more than 5 matches are found, return the best 5 matches sorted according to these rules: 
		1. A Restaurant Name match is defined as an exact or partial String match with what users provided. For example, "Her" would match “Herbed Table” and “Herbed Delicious” restaurants. 
		1. A Customer Rating match is defined as a Customer Rating equal to or more than what users have asked for. For example, “3” would match all the 3 stars restaurants plus all the 4 stars and 5 stars restaurants.
		1. A Distance match is defined as a Distance equal to or less than what users have asked for. For example, “2” would match any distance that is equal to or less than 2 miles from your company.
		1. A Price match is defined as a Price equal to or less than what users have asked for. For example, “15” would match any price that is equal to or less than $15 per person.
		1. A Cuisine match is defined as an exact or partial String match with what users provided. For example, “Chi” would match “Chinese”. You can find all the possible Cuisines in the cuisines.csv file. Each restaurant offers only one cuisine.
		1. The five parameters are holding an “AND” relationship. For example, if users provide Name = “Mcdonald’s” and Distance = 2, you will find all “Mcdonald’s” within 2 miles.
		1. When multiple matches are found, the function sorts them as described below:
			1. First, sort the restaurants by Distance;
			1. After the above process, if two matches are still equal, then the restaurant with a higher customer rating wins;
			1. After the above process, if two matches are still equal, then the restaurant with a lower price wins;
			1. After the above process, if two matches are still equal, then the restaurant name wins. Example: if the input is Customer Rating = 3 and Price = 15. Mcdonald’s is 4 stars with an average spend = $10, and it is 1 mile away. And KFC is 3 stars with an average spend = $8, and it is 1 mile away. Then we should consider Mcdonald’s as a better match than KFC. (They both matches the search criteria -> we compare distance -> we get a tie -> we then compare customer rating -> Mcdonald’s wins).


## Getting Started 
Follow these steps to run the application: 
1. **Prerequisites:**
    - Java Development Kit (JDK) version 17; 
    - Maven (for dependency management). 

2. **Clone the Repository:** 
    ```bash 
    git clone https://github.com/prisoares/best-matched-restaurants-ps.git
    ```
    
3. **Build the Application:**
    Navigate to the project directory and build the application using Maven. 

    ```Bash 
    cd best-matched-restaurants-ps 
    mvn clean install
    ```
    
    
4. **Run the Application:** 
    Execute the Sprint Boot application by running this from your terminal:

    ```Bash 
    mvn spring-boot:run 
    ```
    
5. **Access the Application:**
    The application will be running locally at `http://localhost:8080`. You can access the API endpoints to perform restaurant searches by swagger:

## Swagger Page
http://localhost:8080/swagger-ui/index.html

## API Endpoints 

1. Search Best Restaurants: 
    - *Endpoint:* /api/restaurants/search 
    - *Method:* GET 
    - *Request Body:* JSON with parameters for the search 

### Example Usage 
To search for a restaurant that contains a name "Table" a rating of 4 stars, within 5 miles, an average price of $20 per person, and serving Mexican cuisine, you can make a GET request to `/api/restaurants/search` with the following parameters: 

**Sample Request Parameters:**
`distance=5&customer_rating=4&price=20&restaurant_name=table&cuisine=mexican`

**Sample complete URL with Parameters:**

URL `http://localhost:8080/api/restaurants/search?distance=5&customer_rating=4&price=20&restaurant_name=table&cuisine=mexican`

**Possible Responses:** 

List of the up to 5 restaurants that match the criteria. 

## Error Handling: 
    - If some of the provided parameters are invalid, the API will return an error message with details on which parameter is incorrect. 
    

Feel free to explore the application and customize your restaurant searches based on your preferences. 

**Note:** Make sure to have the `Cusine.csv` and `Restaurant.csv` files in the application's classpath for successful data reading. 

You can customize the data files as needed. 
Enjoy using the Restaurant Search Application! 

If you encounter any issues or have suggestions for improvements, please feel free to reach out some of the developers.