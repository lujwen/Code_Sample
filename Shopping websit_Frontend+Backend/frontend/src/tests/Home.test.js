import { render, screen } from "@testing-library/react";
import Home from "../screens/HomeScreen";
import { MemoryRouter } from "react-router-dom";
import '@testing-library/jest-dom/extend-expect';
import userEvent from "@testing-library/user-event";
import HomeScreen from "../screens/HomeScreen";
import React from "react";


let mockIsAuthenticated = false;
const mockLoginWithRedirect = jest.fn();
const mockUseNavigate = jest.fn();

jest.mock("@auth0/auth0-react", () => ({
    ...jest.requireActual("@auth0/auth0-react"),
    Auth0Provider: ({ children }) => children,
    useAuth0: () => {
      return {
        isLoading: false,
        user: { sub: "foobar" },
        isAuthenticated: mockIsAuthenticated,
        loginWithRedirect: mockLoginWithRedirect,
      };
    },
  }));

  jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: () => {
    return mockUseNavigate;
  },
}));

jest.mock("react-router-dom", () => ({
    ...jest.requireActual("react-router-dom"),
    useNavigate: () => {
      return mockUseNavigate;
    },
  }));

test("renders Home text test", () => {
    render(
      <MemoryRouter initialEntries={["/"]}>
        <HomeScreen />
      </MemoryRouter>
    );
  
    expect(screen.getByText("Our Latest Arrivals")).toBeInTheDocument();
    expect(screen.getByText("Welcome to the best antique shop in North America!")).toBeInTheDocument();
    
});

// test("renders Home button test", () => {
//   render(
//     <MemoryRouter initialEntries={["/"]}>
//       <HomeScreen />
//     </MemoryRouter>
//   );
//     const enterCartButton = screen.getByText("Cart");
//     userEvent.click(enterCartButton);
//     expect(mockUseNavigate).toHaveBeenCalledWith("/cart");

// });
