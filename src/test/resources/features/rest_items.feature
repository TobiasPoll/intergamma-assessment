Feature: Basic cuke

  Background:
    Given all shops are removed from the database
    Given all products are removed from the database
    Given all items are removed from the database

  Scenario: Create a product item for a shop through rest
    Given I have the following shops:
      | id        | name                    | address                           |
      | gamma_uov | Gamma Utrecht Overvecht | Nebraskadreef 18, 3565 AG Utrecht |
    And I have the following products:
      | id          | name        | description              | price |
      | verf_bruin1 | Bruine Verf | Mooie walnootbruine verf | 14.95 |
    When I make a post request to endpoint "/items" with item:
      | id   | shop_id   | product_id  |
      | 1234 | gamma_uov | verf_bruin1 |
    Then the response has status code 200
    And the response has the item:
      | id   | shop_id   | product_id  | price | reserved |
      | 1234 | gamma_uov | verf_bruin1 | 14.95 | false    |

  Scenario: Reserve a product item for a shop through rest
    Given I have the following shops:
      | id        | name                    | address                           |
      | gamma_uov | Gamma Utrecht Overvecht | Nebraskadreef 18, 3565 AG Utrecht |
    And I have the following products:
      | id          | name        | description              | price |
      | verf_bruin1 | Bruine Verf | Mooie walnootbruine verf | 14.95 |
    And I have the following items:
      | id   | shop_id   | product_id  |
      | 1234 | gamma_uov | verf_bruin1 |
    When I make a post request to endpoint "/items/reserve/1234"
    Then the response has status code 200
    When I make a delete request to endpoint "/items/1234"
    Then the response has status code 423
    When the reservation scheduler has run 5 minutes later
    When I make a delete request to endpoint "/items/1234"
    Then the response has status code 200